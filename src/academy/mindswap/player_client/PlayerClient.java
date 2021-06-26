package academy.mindswap.player_client;

import static academy.mindswap.game.messages.GameMessages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class PlayerClient {
    private Socket playerSocket;
    public boolean isPlayerTurn;

    public PlayerClient() {
        this.playerSocket = null;
        isPlayerTurn = false;
    }

    public static void main(String[] args) {
        PlayerClient playerClient = new PlayerClient();
        try {
            playerClient.startPlay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlay() throws IOException {
        playerSocket = new Socket(InetAddress.getLocalHost(), 8080);
        //playerSocket = new Socket("2.tcp.ngrok.io", 19315);
        new Thread(new SendMessages()).start();
        receiveMessageGame();
    }

    private boolean checkCommandStart(char letter) {
        return (letter == "/".charAt(0));
    }

    /**
     * Checks if building command
     *
     * @param letter the character that is going to be tested
     * @return true if is a character false if is newline \n or carriage-return \r\n
     */
    private boolean checkCommandEnd(char letter) {
        return (String.valueOf(letter).matches("."));
    }


    private boolean canTalk(String command) {
        return command.equalsIgnoreCase(PERMISSION_TO_TALK);
    }


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
                    command.delete(0, command.length());
                }
            } else {
                System.out.print(letter);
            }
        }
        quit();
    }

    private void quit(){
        System.out.println("Server Closed Connection");
        try {
            playerSocket.close();
        }
        catch (IOException e){
           System.out.println("Socket already closed");
       }

        System.exit(0);

    }
    private class SendMessages implements Runnable {
        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                BufferedReader inr = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = inr.readLine())!=null) {
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