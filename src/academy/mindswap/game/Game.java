/*
 * @(#)Game.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.game;

import static academy.mindswap.game.messages.GameMessages.*;

import academy.mindswap.game.commands.Command;
import academy.mindswap.game.ascii_art.Board;
import academy.mindswap.game.wheel.NoWheelException;
import academy.mindswap.game.wheel.NullGameException;
import academy.mindswap.game.wheel.Wheel;
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

/**
 * Game class implements the interface Runnable, create a thread for each player and connect them to the game.
 */

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

    /**
     * Starts the game and creates a new wheel all the time that a new game starts.
     * Ensures that the game remains active as long as there is a started Game.
     */
    @Override
    public void run() {
        while (!isGameEnded) {

            if (checkIfGameCanStart() && !isGameStarted) {
                startGame();
                wheel = new Wheel();
                wheel.createWheel(30, 0.25);
            }
            if (isGameStarted && !isGameEnded) {
                try {
                    doTurn();
                }catch (NoWheelException | NullGameException e){
                    System.out.println(e.getMessage());
                    endGame();
                }

            }
        }
        endGame();
    }

    /**
     * Goes to the listOfPlayers and checks if the number of players connected to the game is less than
     * the MAX_NUM_OF_PLAYERS allowed and if the game has not started
     *
     * @return returns true if there is a game that can accept a player false if is full or the game has already started
     */
    public boolean isAcceptingPlayers() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS && !isGameStarted;
    }

    /**
     * A new player connects to the game and sends it to the execution service as a new thread
     *
     * @param playerSocket the socket from the client that connected
     */
    public void acceptPlayer(Socket playerSocket) {
        service.submit(new PlayerHandler(playerSocket));
    }

    /**
     * Adds to the listOfPlayers a new player and send a message to that player
     *
     * @param playerHandler the player that connects
     */
    public synchronized void addPlayerToList(PlayerHandler playerHandler) {
        listOfPlayers.add(playerHandler);
        playerHandler.send(WELCOME_MESSAGE);
    }

    /**
     * Checks if a game can start
     *
     * @return returns true if the game can start or false if there isn't enough players connected to start a game
     */
    private synchronized boolean checkIfGameCanStart() {
        return !isAcceptingPlayers()
                && listOfPlayers.stream().filter(p -> !p.hasLeft)
                .noneMatch(playerHandler -> playerHandler.getName() == null);
    }

    /**
     * Start the Game, add a quote to the game and send messages to all players
     */
    public void startGame() {
        isGameStarted = true;
        addQuoteToList();
        broadcast(START_GAME);
        quoteToGuess = generateRandomQuote();
        broadcast(THIS_IS_THE_QUOTE);
        broadcast(drawBoard());
    }

    /**
     * Go through the listOfPlayers and check if the player has not left the game.
     * Informs all players when it is their turn and which player is playing.
     * Initiates the spinWheel method and send a message to all players with the results of the wheel in each turn
     * Send to the players the quote actualized with the found char in each turn
     * Informs all the players the cash of each player.
     * Check if the game is ended.
     * @throws NoWheelException when no wheel was created before invoking spinWheel() method
     * @throws NullGameException when the provided to wheel.animate() game object is null
     */
    private synchronized void doTurn() throws NoWheelException, NullGameException {
        for (PlayerHandler playerHandler : listOfPlayers) {
            if (!playerHandler.hasLeft()) {

                broadcast(String.format(PLAYER_TURN, playerHandler.getName()));

                Command command = wheel.spinWheel();
                broadcast(SPIN_WHEEL);
                wheel.animate(command, 1, this);

                try {
                    command.getHandler().execute(this, playerHandler);
                } catch (NullPointerException e) {
                    System.out.println("Player didn't existed");
                    continue;
                }
                broadcast(drawBoard());
               // broadcast(getPlayersCash());
            }
            if (isGameEnded) {
                return;
            }
        }
    }

    /**
     * Accesses the resources folder where the quotes are stored and add each quote to the gameQuotes list
     */
    private void addQuoteToList() {
        try {
            Files.lines(Paths.get("resources/WheelOfFortune"))
                    .forEach(gameQuotes::add);
        } catch (IOException e) {
            System.out.println("Could not read game quotes.");
            endGame();
        }
    }

    /**
     * Selects a random quote from the gameQuotes list.
     *
     * @return Returns the quote selected
     */
    private String generateRandomQuote() {
        int index = (int) (Math.random() * gameQuotes.size());

        return gameQuotes.get(index);
    }

    /**
     * Draws the main board game with game TITLE, quote to guess and player's cash
     *
     * @return Returns the actualized quote with the discover letters
     */
    public String drawBoard() {
        List<List<String>> players= listOfPlayers.stream().
                map(p-> Arrays.asList(p.getName(),p.getPlayerCash()+"€"))
                .collect(Collectors.toList());
        return Board.drawBoard(prepareQuoteToGame(), playerLetters, players);
    }

    /**
     * Prepares the quote to the game by replacing all the letters that wasn't discover by the players in #
     *
     * @return Returns the actualized quote with the discover letters
     */
    private String prepareQuoteToGame(){
        String regex = String.join("", playerLetters);
        return Arrays.stream(quoteToGuess.split(""))
                .map(c -> c = c.toLowerCase().matches("[" + regex + "|[^a-z]]") ? c : "#")
                .collect(Collectors.joining());
    }

    /**
     * Stores the letters that was chosen by players in playerLetters
     *
     * @param letter is the letter chosen by player in game in each turn
     */
    public void addPlayerLetters(String letter) {
        this.playerLetters.add(letter);
    }


    public String getListOfChosenLetters() {
        return String.join(", ", playerLetters);
    }

    /**
     * Getter for the quote to guess in game
     *
     * @return Returns the quote
     */
    public String getQuoteToGuess() {
        return quoteToGuess;
    }

    /**
     * Get the cash of each player and send the result to the players
     *
     * @return returns the player cash
     */
    public String getPlayersCash() {
        return listOfPlayers.stream()
                .map(p -> String.format(PLAYER_CASH, p.getName(), p.getPlayerCash()))
                .collect(Collectors.joining(" || ")).concat("\n\n");
    }

    /**
     * Send message to all players that are connected to the game
     *
     * @param message message to send
     */
    public synchronized void broadcast(String message) {
        listOfPlayers.stream()
                .filter(p -> !p.hasLeft)
                .forEach(player -> player.send(message));
    }

    /**
     * Send message to all players except one
     *
     * @param message        message to send
     * @param doNotBroadcast is player that not receive the message
     */
    public synchronized void broadcast(String message, PlayerHandler doNotBroadcast) {
        listOfPlayers.stream()
                .filter(p -> !p.hasLeft)
                .filter(p -> !p.equals(doNotBroadcast))
                .forEach(player -> player.send(message));
    }

    /**
     * Verify if there is players remaining in game, if there is no players the game will ends
     */
    public void areStillPlayersPlaying() {
        if (listOfPlayers.stream()
                .filter(p -> p.hasLeft).count() == MAX_NUM_OF_PLAYERS) {
            endGame();
        }
    }

    /**
     * Ends the game and removes it from the server list
     * send a message for all players to inform that the game has ended
     * Go through the listOfPlayers and forces all the players to quit the game
     */
    public void endGame() {
        removeFromServerList();
        broadcast(GAME_END);
        listOfPlayers.stream()
                .filter(p -> !p.hasLeft)
                .forEach(PlayerHandler::quit);
        isGameEnded = true;
    }

    /**
     * Removes the game from server
     */
    public void removeFromServerList() {
        server.removeGameFromList(this);
    }

    /**
     *
     */
    public class PlayerHandler implements Runnable {

        private String name = null;
        private Socket playerSocket;
        private PrintWriter out;
        private int playerCash;
        private String message;
        private boolean hasLeft;
        BufferedReader in;

        /**
         * Constructor Method
         * playerCash propertie is the cash that each player
         * BufferedReader to read the player input
         * PrintWriter to send a output to player
         * @param playerSocket the socket from the player
         */
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

        /**
         * Add player to the game
         * verify is player is not left the game, if its true send a message asking his/her name
         * Send a message to all players to inform that a new player have arrived
         * Checks if in the end of game the thread was interrupted and forces the players to quit
         */
        @Override
        public void run() {

            addPlayerToList(this);
            send(ASK_NAME);
            name = getAnswer();
            if (name != null) {// if null player left the game by closing connection
                broadcast(String.format(PLAYER_JOINED, name));
            }
            send(WAITING_FOR_OTHER_PLAYERS);
            while (!isGameEnded) {
                if (Thread.interrupted()) {
                    return;
                }
            }
            quit();

        }

        /**
         * Get the player answer
         * @return Returns the answer from the player
         */
        public String getAnswer() {
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException | NullPointerException e) {
                quit();
            } finally {
                if (message == null) {
                    quit();
                }
            }
            return message;
        }

        /**
         * Send the message to the player
         * @param message message to send
         */
        public void send(String message) {
            out.write(message); // send char by char to allow animate in the playerClient side
            out.flush();
        }

        /**
         * Add cash money of the player
         * @param bonus is the amount of money
         */
        public void addCash(int bonus) {
            playerCash += bonus;
        }

        /**
         * Remove cash money of the player
         * @param bonus is the amount of money
         */
        public void removeCash(int bonus) {
            playerCash -= bonus;
        }


        public String getName() {
            return name;
        }

        public int getPlayerCash() {
            return playerCash;
        }

        /**
         * Verify if the player has left the game
         * @return Return true if the player has left the game or false if its not
         */
        public boolean hasLeft() {
            return hasLeft;
        }

        /**
         * If the player left the game the playerSocket will close
         * Verify if there is players remaining in game and inform them the player that left
         */
        public void quit() {
            hasLeft = true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't closer player socket");
            } finally {
                areStillPlayersPlaying();
                broadcast(String.format(PLAYER_LEFT_GAME, name));
            }
        }
    }

}
