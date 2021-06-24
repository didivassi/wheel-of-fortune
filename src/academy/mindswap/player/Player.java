package academy.mindswap.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Player {

    private Socket playerSocket;
    private String name;
    private boolean isPlayerTurn;

    public Player(String name) {
        this.playerSocket = null;
        this.name = name;
        isPlayerTurn = false;
    }

    public static void main(String[] args) {
        Player player1 = new Player("Ana");
        try {
            player1.startPlay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is responsible for preparing the player for the game.
     * It opens a socket for communicating with the server
     * It launches a new thread responsible for sending messages to the server
     * It starts to receive messages from the server
     *
     * @throws IOException when a socket can't be opened
     */
    private void startPlay() throws IOException {

        playerSocket = new Socket(InetAddress.getLocalHost(), 8080);
        new Thread(new SendMessages()).start();
        receiveMessageGame();
    }

    /**
     * Checks if the server is starting sending a command
     *
     * @param letter the character that is going to be tested
     * @return true if character is "/" false other character
     */
    private boolean checkCommandStart(char letter) {
        return (letter == "/".charAt(0));
    }

    /**
     * Checks if we are building command that was send by the server
     *
     * @param letter the character that is going to be tested
     * @return true if is a character false if is newline \n or carriage-return \r\n
     */
    private boolean checkCommandEnd(char letter) {
        return (String.valueOf(letter).matches("."));
    }

    /**
     * Checks if we the command received is /talk now
     *
     * @param command the command that is going to be tested
     * @return true if command is /talk now
     */
    private boolean canTalk(String command) {
        return command.equalsIgnoreCase("/talk now");
    }

    /**
     * This is the method responsible for receiving and printing the messages sent by the server.
     * It prints char by char on the console to allow animations in the same line using the \r
     *
     * @throws IOException When the BufferedReader can't read()
     */
    private void receiveMessageGame() throws IOException {

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
                    setPlayerTurn(canTalk(command.toString()));
                }
            } else {
                System.out.print(letter);
            }
        }

    }

    public synchronized void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    /**
     * This method is responsible for reading the commands that the player will send to the server.
     *
     * @return a String with the player input
     * @throws IOException when BufferedReader can't readLine()
     */
    private String receiveMessagesFromConsole() throws IOException {

        String messageInLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        messageInLine = in.readLine();
        return messageInLine;
    }


    /**
     * The class responsible for sending messages to the server
     * It only allows to send message if server sends a /talk now command
     */
    private class SendMessages implements Runnable {

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                while (!playerSocket.isClosed()) {
                    if (isPlayerTurn) {
                        out.println(receiveMessagesFromConsole());
                    }
                    setPlayerTurn(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
