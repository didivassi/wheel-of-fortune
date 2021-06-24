package academy.mindswap.game;

import static academy.mindswap.messages.Messages.*;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game {

    private static final int MAX_NUM_OF_PLAYERS = 3;

    private List<PlayerHandler> listOfPlayers;
    private ExecutorService service;

    public void acceptPlayer(Socket playerSocket) {
        int numberOfConnections = 0;
        service = Executors.newFixedThreadPool(3);
        service.submit(new PlayerHandler(playerSocket, DEFAULT_NAME + ++numberOfConnections));
    }
    public void addPlayerToList(PlayerHandler playerHandler) {
        listOfPlayers.add(playerHandler);
        playerHandler.send(PLAYER_ENTERED_GAME);
        //broadcast(playerHandler.getName(), )

    }
    public boolean isFull() {
        return listOfPlayers.size() == MAX_NUM_OF_PLAYERS;
    }
    /*public void spinWheel(ConsoleHelper animate) {
        animate ADICIONAR A CLASS ANIMATE
    }*/


   public class PlayerHandler implements Runnable {

        private String name;
        private Socket playerSocket;
        private PrintWriter out;


        public PlayerHandler (Socket playerSocket, String name) {
            this.playerSocket = playerSocket;
            this.name = name;
        }

       @Override
       public void run() {
           addPlayerToList(this);
       }
       public void send(String message) {
            out.print(message);
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
