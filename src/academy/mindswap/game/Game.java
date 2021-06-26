package academy.mindswap.game;


import static academy.mindswap.game.messages.GameMessages.*;
import academy.mindswap.game.commands.Command;
import academy.mindswap.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class Game implements Runnable {

    private static final int MAX_NUM_OF_PLAYERS = 3;
    private final Server server;
    private final List<String> gameQuotes;
    private volatile List<PlayerHandler> listOfPlayers;
    private final ExecutorService service;
    private volatile boolean isGameEnded;
    private volatile boolean isGameStarted;
    private String quoteToGuess;
    private final Set<String> playerLetters;
    private Wheel wheel;

    public Game(Server server) {
        this.server = server;
        service = Executors.newFixedThreadPool(MAX_NUM_OF_PLAYERS);
        listOfPlayers = new ArrayList<>();
        gameQuotes = new ArrayList<>();
        isGameEnded = false;
        isGameStarted = false;
        playerLetters = new HashSet<>();

    }

    @Override
    public void run() {
        while (!isGameEnded) {
            try {
                if (checkIfGameCanStart() && !isGameStarted) {
                    startGame();
                    wheel = new Wheel();
                    wheel.createWheel(30, 0.25);
                }
            } catch (IOException e) {
                e.printStackTrace();
                endGame();
            }
            if (isGameStarted && !isGameEnded) {
                doTurn();
            }
        }
        endGame();
    }

    public boolean isAcceptingPlayers() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS && !isGameStarted;
    }

    public void acceptPlayer(Socket playerSocket) {
        service.submit(new PlayerHandler(playerSocket));
    }

    public synchronized void addPlayerToList(PlayerHandler playerHandler) throws IOException {
        listOfPlayers.add(playerHandler);
        playerHandler.send(WELCOME_MESSAGE);
    }

    private synchronized boolean checkIfGameCanStart() {
        return !isAcceptingPlayers()
                && listOfPlayers.stream().filter(p -> !p.hasLef)
                .noneMatch(playerHandler -> playerHandler.getName() == null);
    }

    public void startGame() throws IOException {
        isGameStarted = true;
        addQuoteToList();
        broadcast(START_GAME);
        quoteToGuess = generateRandomQuote();
        broadcast(prepareQuoteToGame());
    }

    private synchronized void doTurn() {
        for (PlayerHandler playerHandler : listOfPlayers) {
            if(!playerHandler.hasLef()){
                broadcast(String.format(PLAYER_TURN, playerHandler.getName()));
                Command command = wheel.spinWheel();
                wheel.animate(command, 1, this);
                try {
                    command.getHandler().execute(this, playerHandler);
                }catch (NullPointerException e){
                    System.out.println("Player didn't existed");
                }
            }
            if (isGameEnded) {
                return;
            }
        }
    }

    private void addQuoteToList() throws IOException {
        Files.lines(Paths.get("resources/WheelOfFortune"))
                .forEach(gameQuotes::add);
    }

    private String generateRandomQuote() {
        int index = (int) (Math.random() * gameQuotes.size());

        return gameQuotes.get(index) + "\n";
    }

    public String prepareQuoteToGame() {
        String regex = String.join("", playerLetters);

        return Arrays.stream(quoteToGuess.split(""))
                .map(c -> c = c.toLowerCase().matches("[" + regex + "|[^a-z]]") ? c : "#")
                .collect(Collectors.joining());
    }

    public void addPlayerLetters(String letter) {
        this.playerLetters.add(letter);
    }

    public String getListOfChosenLetters() {
        return String.join(", ", playerLetters);
    }

    public String getQuoteToGuess() {
        return quoteToGuess;
    }

    public synchronized void broadcast(String message) {
        listOfPlayers.stream()
                .filter(p ->!p.hasLef)
                .forEach(player -> player.send(message));
    }

    public void areStillPlayersPlaying(){
       if( listOfPlayers.stream()
                .filter(p ->p.hasLef).count() == MAX_NUM_OF_PLAYERS) {
           endGame();
       }
    }

    public void endGame() {
        removeFromServerList();
        broadcast(GAME_END);
        listOfPlayers.stream()
                .filter(p ->!p.hasLef)
                .forEach(PlayerHandler::quit);
        isGameEnded = true;
    }

    public void removeFromServerList() {
        server.removeGameFromList(this);
    }

    public class PlayerHandler implements Runnable {

        private String name = null;
        private Socket playerSocket;
        private PrintWriter out;
        private int playerCash;
        private String message;
        private boolean hasLef;
        BufferedReader in;

        public PlayerHandler(Socket playerSocket) {
            this.playerSocket = playerSocket;
            playerCash = 0;
            try {
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new PrintWriter(playerSocket.getOutputStream(), true);
            } catch (IOException e) {
                quit();
            }
        }

        @Override
        public void run() {

            try {
                addPlayerToList(this);
                send(ASK_NAME);
                name = getAnswer();
                if(name!=null){// if null player left the game by closing connection
                    broadcast(String.format(PLAYER_JOINED,name));
                }

                while (!isGameEnded){
                    if (Thread.interrupted()) {
                        return;
                    }
                }

            } catch (IOException e) {
                quit();
            }
        }

        public String getAnswer() {
            String message=null;
            try {
                 message=in.readLine();
            } catch (IOException | NullPointerException e) {
                quit();
            }finally {
                if(message==null){
                    quit();
                }
            }
            return message;
        }

        public void send(String message) {
            out.write(message);
            out.flush();
        }

        public void addCash(int bonus) {
            playerCash += bonus;
        }

        public void removeCash(int bonus) {
            playerCash -= bonus;
        }

        public String getName() {
            return name;
        }

        public int getPlayerCash() {
            return playerCash;
        }

        public boolean hasLef() {
            return hasLef;
        }

        public void quit() {
            hasLef=true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't closer player socket");
            }finally {
                areStillPlayersPlaying();
                broadcast(String.format(PLAYER_LEFT_GAME, name));
            }
        }
    }


}
