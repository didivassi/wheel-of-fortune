package academy.mindswap.server;

import academy.mindswap.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class Server {

    private ExecutorService gamesService;
    private final List<Game> gameList;

    public Server() {
        gameList = new LinkedList<>();
    }

    /**
     * Main method of the class Server
     * Accepts a port as an argument. If no port is provided the default is 8080
     * @param args the port to start the server
     */
    public static void main(String[] args) {
        Server server = new Server();
        int port;
        try {
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            port=8080;
        }

        try {
            server.start(port);
        }catch (RejectedExecutionException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Starts the server in the specified port
     * If there is no game available for players to join creates a new one
     * Waits for a client to connect to the server and add it to an available game
     * @param port the port that will accept client connections
     * @throws RejectedExecutionException the game thread couldn't start
     * @throws IOException the server socket couldn't start
     */
    public void start(int port) throws RejectedExecutionException, IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        gamesService = Executors.newCachedThreadPool();

        while (serverSocket.isBound()) {
            if(!isGameAvailable()){
                createGame();
            }
            if(getAvailableGame().isPresent()){
               getAvailableGame().get().acceptPlayer(serverSocket.accept());
            }
        }
    }

    /**
     * Creates a new game and sends it to the execution service as a new thread
     * Adds the created game to the gameList of the server
     * @throws RejectedExecutionException the thread couldn't start
     */
    private void createGame() throws RejectedExecutionException {
        Game game=new Game();
        //gamesService.submit(game);
        gameList.add(game);
    }

    /**
     * Goes through the gameList of the server and checks if there is a game that can accept a player
     * @return returns true if there is a game that can accept a player false if all games are full
     */
    private boolean isGameAvailable(){
       return gameList.stream().anyMatch(game ->!game.isFull());
    }

    /**
     * Goes through the gameList of the server and returns an available game
     * @return returns the first available game that can accept a player
     */
    private Optional<Game> getAvailableGame(){
        return gameList.stream().filter(game ->!game.isFull()).findFirst();
    }


}
