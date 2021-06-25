package academy.mindswap.game;


import static academy.mindswap.messages.Messages.*;

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

//retornar frase
// metodo frase
//split e replace por char
// start game
public class Game implements Runnable {

    private static final int MAX_NUM_OF_PLAYERS = 3;

    private volatile List<PlayerHandler> listOfPlayers;
    ExecutorService service;
    private final Server server;
    private final List<String> gameQuotes;
    private volatile boolean isGameEnded;
    private volatile boolean isGameStarted;
    private String quoteToGuess;
    Set<String> playerLetters;
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
            }
            if (isGameStarted && !isGameEnded) {
                doTurn();
            }
        }

    }

    public synchronized void acceptPlayer(Socket playerSocket) {
        service.submit(new PlayerHandler(playerSocket));
    }

    public synchronized void addPlayerToList(PlayerHandler playerHandler) throws IOException {
        listOfPlayers.add(playerHandler);
        playerHandler.send(WELCOME_MESSAGE);
    }

    public synchronized void removePlayerFromList(PlayerHandler playerHandler) {
        listOfPlayers.remove(playerHandler);
    }

    public synchronized void broadcast(String message) {
        listOfPlayers.stream()
                .filter(p ->!p.hasLef)
                .forEach(player -> player.send(message));
    }

    public synchronized boolean isAcceptingPlayers() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS && !isGameStarted;
    }

    private boolean checkIfGameCanStart() {
        return !isAcceptingPlayers() && listOfPlayers.stream().noneMatch(playerHandler -> playerHandler.getName() == null);
    }

    private synchronized void doTurn() {


        for (PlayerHandler playerHandler : listOfPlayers) {
            broadcast(String.format(PLAYER_TURN, playerHandler.getName()));
            Command command = wheel.spinWheel();
            wheel.animate(command, 1, this);
            if(!playerHandler.hasLef()){
                command.getHandler().execute(this, playerHandler);
            }
            if (isGameEnded) {
                return;
            }
        }
    }

    /*public void spinWheel(ConsoleHelper animate) {
        animate ADICIONAR A CLASS ANIMATE
    }*/

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
                .map(c -> c = c.toLowerCase().matches("[" + regex + "||[^a-z]]") ? c : "#")
                .collect(Collectors.joining());
    }

    public synchronized void startGame() throws IOException {
        isGameStarted = true;
        addQuoteToList();
        broadcast(START_GAME);
        quoteToGuess = generateRandomQuote();
        broadcast(prepareQuoteToGame());
    }

    public void addPlayerLetters(String letter) {
        this.playerLetters.add(letter);
    }

    public String getListOfChosenLetters() {
        return String.join(", ", playerLetters);
    }

    public void removeFromServerList() {
        server.removeGameFromList(this);
    }

    public String getQuoteToGuess() {
        return quoteToGuess;
    }


    public void endGame() {
        removeFromServerList();
        broadcast(GAME_END);
        listOfPlayers.forEach(playerHandler -> {
            try {
                playerHandler.playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        isGameEnded = true;
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
                out = new PrintWriter(playerSocket.getOutputStream(), true);
            } catch (IOException e) {
                quit();
            }
        }

        @Override
        public void run() {

            try {
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                addPlayerToList(this);
                send(ASK_NAME);
                name = in.readLine();
                while (!isGameEnded) ;

            } catch (IOException e) {
                quit();
            }
        }

        public String getAnswer() {
            try {
                return in.readLine();
            } catch (IOException | NullPointerException e) {
                quit();
                //e.printStackTrace();
            }
            return "";
        }

        private void quit() {
           // removePlayerFromList(this);
            hasLef=true;
            try {
                playerSocket.close();

            } catch (IOException e) {

            }finally {
                broadcast(String.format(PLAYER_LEFT_GAME, name));
            }

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
    }


}
