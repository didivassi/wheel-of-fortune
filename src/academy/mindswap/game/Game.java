package academy.mindswap.game;

import academy.mindswap.player.Player;

import java.net.Socket;
import java.util.ArrayList;

public class Game implements Runnable {

    private static final int MAX_NUM_OF_PLAYERS = 3;

    private ArrayList <Socket> listOfPlayers;
    private int[] playersScore;

    public Game() {

    }

    public boolean isFull() {
        return listOfPlayers.size() == MAX_NUM_OF_PLAYERS;
    }
    public void addPlayers(Socket playerSocket) {
        listOfPlayers.add(playerSocket);
    }

    @Override
    public void run() {

    }

    /*public String getName() {
        return
    }*/


    /*START()
    SPINWHEEL()
    askForPhrase()
    games starts
    game aks server a phrase (game replace the words for #)
    game broadcasts the phrase to all players*/

}
