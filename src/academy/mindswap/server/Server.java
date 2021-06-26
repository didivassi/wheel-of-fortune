/*
 * @(#)Server.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.server;

import static academy.mindswap.server.Messages.*;
import academy.mindswap.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 *Class responsible for creating the server that will start a game and listen to players
 * When a player arrives his socket will be sent to the available game
 * If a game is already full and not accepting players it creates a new game to accept new players.
 *
 */
public class Server {

    private ExecutorService gamesService;
    private final List<Game> gameList;

    /**
     * Constructor method from Server class
     * Creates a list that will hold the active games
     */
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
        int port = server.getServerPort(args);

        try {
            server.start(port);
        }catch (RejectedExecutionException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses the args given to main method.
     * Exits with error 1 if an illegal port number was provided as argument
     * @param args the arguments provided to main method
     * @return port 8080 if no port was provided to main, otherwise uses the provided port
     */
    private int getServerPort(String[] args){
        int port = 8080;
        try {
            if (args.length>0) {
                port = Integer.parseInt(args[0]);
            }
        }catch (NumberFormatException e) {
            System.out.println(NOT_VALID_ARGS);
            System.exit(1);
        }
        return port;
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

        System.out.printf(SERVER_PORT, port);

        while (serverSocket.isBound()) {

            if (!isGameAvailable() ){
                createGame();
                System.out.println(NEW_GAME);
            }

            if (getAvailableGame().isPresent()) {
                getAvailableGame().get().acceptPlayer(serverSocket.accept());
                System.out.println(PLAYER_ADDED_TO_GAME);

              try{ //Give some time to start another game or an error will occur
                   Thread.sleep(40);
               }catch (InterruptedException e) {
                  System.out.println(e.getMessage());
               }

            }
        }
    }

    /**
     * Creates a new game and sends it to the execution service as a new thread
     * Adds the created game to the gameList of the server
     * @throws RejectedExecutionException the thread couldn't start
     */
    private void createGame() throws RejectedExecutionException {
        Game game=new Game(this);
        gameList.add(game);
        gamesService.execute(game);
    }

    /**
     * Goes through the gameList of the server and checks if there is a game that can accept a player
     * @return returns true if there is a game that can accept a player false if all games are full
     */
    private synchronized boolean isGameAvailable(){
       return gameList.stream().anyMatch(Game::isAcceptingPlayers);
    }

    /**
     * Goes through the gameList of the server and returns an available game
     * @return returns the first available game that can accept a player
     */
    private synchronized Optional<Game> getAvailableGame() {
        return gameList.stream().filter(Game::isAcceptingPlayers).findFirst();
    }

    /**
     * Removes a game from the gameList. This method is invoked by the game itself
     * @param game the game object to be removed.
     */
    public synchronized void removeGameFromList(Game game){
        if(gameList.contains(game)){
            gameList.remove(game);
            System.out.println(GAME_REMOVED);
        }
    }

}
