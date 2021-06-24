package academy.mindswap.game;

import static academy.mindswap.messages.Messages.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

    public volatile   List<PlayerHandler> listOfPlayers;
    private final List<String> gameQuotes;
    private volatile boolean isGameEnded;
    private volatile boolean isGameStarted;

    public Game() {
        listOfPlayers = new ArrayList<>();
        gameQuotes = new ArrayList<>();
        isGameEnded=false;
        isGameStarted=false;
    }

    @Override
    public void run() {
        while (!isGameEnded){
            try {
                if(!isGameStarted){
                    checkIfCanStart();
                   // System.out.println("cant start");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void checkIfCanStart() throws IOException {
        if (!isAcceptingPlayers()) {
            startGame();
        }
    }

    public synchronized boolean isAcceptingPlayers() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS;
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
        return Arrays.stream(generateRandomQuote().split(""))
                .map(c -> c = c.equals(" ") ? c : "#")
                .collect(Collectors.joining());
    }

    public void startGame() throws IOException {
        isGameStarted=true;
        addQuoteToList();
        broadcast(START_GAME);
        broadcast(prepareQuoteToGame());
    }




    public class PlayerHandler implements Runnable {

        private String name;
        private Socket playerSocket;
        private PrintWriter out;
        private String message;


        public PlayerHandler(Socket playerSocket) {
            this.playerSocket = playerSocket;
            try {
                out = new PrintWriter(playerSocket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                addPlayerToList(this);
                send(ASK_NAME);
                name = in.readLine();

                while (!playerSocket.isClosed()) {
                    message = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String message) {
            out.write(message);
            out.flush();
        }

        public String getName() {
            return name;
        }
    }


    /*START()
    SPINWHEEL()
    askForPhrase()
    games starts
    game aks server a phrase (game replace the words for #)
    game broadcasts the phrase to all players*/

}
