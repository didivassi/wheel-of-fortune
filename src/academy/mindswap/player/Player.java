package academy.mindswap.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Player {

    private Socket playerSocket;
    public String name;
    public boolean isPlayerTurn;


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


    public void startPlay() throws IOException {

        playerSocket = new Socket(InetAddress.getLocalHost(), 8080);

        new Thread(new SendMessages()).start();


        receiveMessageGame();
    }


    public void receiveMessageGame() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

        while (!playerSocket.isClosed()) {
            String gameMessage = in.readLine();
            if (gameMessage.toLowerCase().startsWith("/talk now!")) {
                isPlayerTurn = true;
                continue;
            }
            System.out.println(gameMessage);
        }
    }


        public String receiveMessagesFromConsole() throws IOException {

            String messageInLine = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (isPlayerTurn) {
                messageInLine = in.readLine();
            }
            return messageInLine;
        }


        private class SendMessages implements Runnable {

            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                    while (!playerSocket.isClosed()) {
                        if (isPlayerTurn) {
                            out.println(receiveMessagesFromConsole());
                        }
                        isPlayerTurn = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
