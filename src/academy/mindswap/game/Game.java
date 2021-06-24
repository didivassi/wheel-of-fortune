package academy.mindswap.game;

import static academy.mindswap.messages.Messages.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static academy.mindswap.messages.Messages.*;

//retornar frase
// metodo frase
//split e replace por char
// start game
public class Game {

    private static final int MAX_NUM_OF_PLAYERS = 3;

    private List<PlayerHandler> listOfPlayers;
    private ExecutorService service;

    public Game(){
        listOfPlayers = new ArrayList<>();
    }
    public void acceptPlayer(Socket playerSocket) {
        int numberOfConnections = 0;
        service = Executors.newFixedThreadPool(MAX_NUM_OF_PLAYERS);
        service.submit(new PlayerHandler(playerSocket, DEFAULT_NAME + ++numberOfConnections));
    }

    public void startGame(){
        //
        //if(service.) // pesquisar documentação da thread para numero de
    }

    public synchronized void  addPlayerToList(PlayerHandler playerHandler) throws InterruptedException {
        System.out.println("entered add players");
        listOfPlayers.add(playerHandler);
        playerHandler.send(PLAYER_ENTERED_GAME);
        //broadcast(playerHandler.getName(), )

    }
    public boolean isAvailable() {
        return listOfPlayers.size() < MAX_NUM_OF_PLAYERS;
    }
    /*public void spinWheel(ConsoleHelper animate) {
        animate ADICIONAR A CLASS ANIMATE
    }*/


   public class PlayerHandler implements Runnable {

        private String name;
        private Socket playerSocket;
        private PrintWriter out;
        private String message;


        public PlayerHandler (Socket playerSocket, String name) {
            this.playerSocket = playerSocket;
            this.name = name;
            try {
                out=new PrintWriter(playerSocket.getOutputStream(), true);
            }
           catch (IOException e){
               System.out.println(e.getMessage());
           }
        }

       @Override
       public void run() {
           addPlayerToList(this);

           try {
               BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

               while(!playerSocket.isClosed()){
                   message = in.readLine();
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       public void send(String message){
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
