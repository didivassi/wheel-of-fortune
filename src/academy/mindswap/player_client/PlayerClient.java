/*
 * @(#)PlayerClient.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.player_client;

import static academy.mindswap.games.factory.wheel_of_fortune.messages.GameMessages.*;
import static academy.mindswap.player_client.PlayerMessages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class PlayerClient {
    private Socket playerSocket;
    public boolean isPlayerTurn;

    /**
     * Constructor method to initialize the properties
     */
    public PlayerClient() {
        this.playerSocket = null;
        isPlayerTurn = false;
    }

    /**
     * Main method of the class PlayerClient
     * Accepts a server address and a port as an argument.
     * If no address is provided the default is localhost
     * If no port is provided the default is 8080
     * Example: java PlayerClient www.server.com 8080
     * @param args the address and the port to start the playerClient
     */
    public static void main(String[] args) throws UnknownHostException {
        PlayerClient playerClient = new PlayerClient();
        InetAddress host = playerClient.getServerHost(args);
        int port = playerClient.getServerPort(args);
        try {
            playerClient.startPlay(host, port);
        } catch (IOException e) {
            System.out.println(NOT_CONNECT_SERVER);
        }
    }

    /**
     * Parses the args given to main method.
     * @param args the arguments provided to main method
     * @return localhost if bo host was provided to main, otherwise uses the provided host
     * @throws UnknownHostException when the host can't be reached
     */
    private InetAddress getServerHost(String[] args) throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        try {
            if (args.length>0) {
                host = InetAddress.getByName(args[0]);
            }
        }catch (NumberFormatException e) {
            System.out.println(NOT_VALID_HOST);
            System.exit(1);
        }
        return host;
    }

    /**
     * Parses the args given to main method.
     * @param args the arguments provided to main method
     * @return port 8080 if no port was provided to main, otherwise uses the provided port
     */
    private int getServerPort(String[] args) {
        int port = 8080;
        try {
            if (args.length>0) {
                port = Integer.parseInt(args[1]);
            }
        }catch (NumberFormatException e) {
            System.out.println(NOT_VALID_PORT);
            System.exit(1);
        }
        return port;
    }

    /**
     * Starts the player in specified port
     * Create a new thread to send messages to game
     * @throws IOException when it's not possible to connect to the server
     */
    public void startPlay(InetAddress host, int port) throws IOException {
        playerSocket = new Socket(host, port);
        new Thread(new SendMessages()).start();
        receiveMessageGame();
    }

    /**
     * Read the message from de game
     * Read and print in console the message from game
     * Check if the message sent have the char with the command for player answer
     * In case of message have the command to player talk this is removed from the message before print in console
     * @throws IOException If socket client closed throws quit
     */
    public void receiveMessageGame() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        StringBuilder command = new StringBuilder();
        int value;
        boolean buildingCommand = false;
        boolean isCommand = false;
        while ((value = in.read()) != -1) {
            char letter = (char) value;
            if (!buildingCommand) {
                isCommand = checkCommandStart(letter);
            }
            if (isCommand) {
                command.append(letter);
                buildingCommand = checkCommandEnd(letter);
                if (!buildingCommand) {
                    isPlayerTurn = canTalk(command.toString());
                    if(!isPlayerTurn){
                        System.out.print(command);
                    }
                    command.delete(0, command.length()); //delete the message in memory
                }
            }else {
                System.out.print(letter);
            }
        }
        quit();
    }

    /**
     * Check if letter character is "/"
     * @param letter the character that is going to be tested
     * @return true if the letter is "/" otherwise false
     */
    private boolean checkCommandStart(char letter) {
        return (letter == "/".charAt(0));
    }

    /**
     * Checks if building command
     * @param letter the character that is going to be tested
     * @return true if is a character false if is newline \n or carriage-return \r\n
     */
    private boolean checkCommandEnd(char letter) {
        return (String.valueOf(letter).matches("."));
    }

    /**
     * Check if the player can talk
     * @param command the string that will be compare
     * @return true if can talk otherwise false
     */
    private boolean canTalk(String command) {
        return command.equalsIgnoreCase(PERMISSION_TO_TALK);
    }

    /**
     * After the server close the socket, the player socket will close and left the game
     */
    private void quit(){
        System.out.println(SERVER_CLOSE);
        try {
            playerSocket.close();
        }
        catch (IOException e){
           System.out.println(CLIENT_CLOSE_SOCKET);
       }

        System.exit(0);

    }

    /**
     * SendMessages Inner class implements the interface Runnable.
     */
    private class SendMessages implements Runnable {
        /**
         * Read the input in console from the player if the player have permission to talk
         */
        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = in.readLine())!=null) {
                    if (isPlayerTurn) {
                        out.println(message);
                    }
                    isPlayerTurn = false;
                }
                quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}