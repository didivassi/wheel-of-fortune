package academy.mindswap.game;


import static academy.mindswap.messages.Messages.*;

import academy.mindswap.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
    private final Server server;
    private final List<String> gameQuotes;
    private boolean isGameEnded;
    private boolean isGameStarted;
    private String quoteToGuess;
    List<String> playerLetters = new LinkedList<>();

    public Game(Server server) {
        this.server = server;
        listOfPlayers = new ArrayList<>();
        gameQuotes = new ArrayList<>();
        isGameEnded = false;
        isGameStarted = false;
        playerLetters = new LinkedList<>();
    }

    @Override
    public void run() {
        while (!isGameEnded) {
            try {
                if (checkIfGameCanStart() && !isGameStarted) {
                    startGame();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (isGameStarted) {
                doTurn();
            }
        }
        removeFromServerList();
    }

    public synchronized void acceptPlayer(Socket playerSocket) {
        ExecutorService service = Executors.newFixedThreadPool(MAX_NUM_OF_PLAYERS);
        service.submit(new PlayerHandler(playerSocket));
    }

    public synchronized void addPlayerToList(PlayerHandler playerHandler) throws IOException {
        listOfPlayers.add(playerHandler);
        playerHandler.send(WELCOME_MESSAGE);
    }

    public void broadcast(String message) {
        listOfPlayers.forEach(player -> player.send(message));
    }

    public synchronized boolean isAcceptingPlayers() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS;
    }

    private boolean checkIfGameCanStart() {
        return !isAcceptingPlayers() && listOfPlayers.stream().noneMatch(playerHandler -> playerHandler.getName() == null);
    }

    private void doTurn() {
        for (PlayerHandler playerHandler : listOfPlayers) {

            //spinWheel();

            playerHandler.send(playerHandler.getName() + CHOOSE_A_LETTER);

            String playerAnswer = playerHandler.getAnswer();
            while (!checkAnswer(playerAnswer)) {
                playerHandler.send(playerHandler.getName() + INVALID_LETTER);
                playerAnswer = playerHandler.getAnswer();
            }
            //findChar(playerAnswer);
            System.out.println(playerAnswer);
            //SPIN WHEEL
            //GET LETTER
            //SHOW RESULTS

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

        String regex= String.join("", playerLetters);
        return Arrays.stream(quoteToGuess.split(""))
                .map(c -> c =c.toLowerCase().matches("["+regex+"||[^a-z]]") ? c : "#")
                .collect(Collectors.joining());
    }

    public boolean checkAnswer(String playerAnswer) {
        if (playerAnswer.length() != 1) {
            return false;
        }
        playerAnswer.toLowerCase().matches("[a-z&&[^aeiou]]");
        return true;
    }


    public void startGame() throws IOException {
        isGameStarted = true;
        addQuoteToList();
        broadcast(START_GAME);
        quoteToGuess = generateRandomQuote();
        broadcast(prepareQuoteToGame());
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
        BufferedReader in;

        public PlayerHandler(Socket playerSocket) {
            this.playerSocket = playerSocket;
            playerCash = 0;
            try {
                out = new PrintWriter(playerSocket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
                e.printStackTrace();
            }
        }

        public String getAnswer() {
            try {
                return in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
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
    }


    /*START()
    SPINWHEEL()
    askForPhrase()
    games starts
    game aks server a phrase (game replace the words for #)
    game broadcasts the phrase to all players*/

}
