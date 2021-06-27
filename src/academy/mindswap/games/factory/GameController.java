/*
 * @(#)GameController.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory;

import java.net.Socket;

/**
 * Interface implemented in game class
 */
public interface GameController extends Runnable {

    /**
     * Verify if the game is accepting players.
     * @return returns true if there is a game that can accept a player false if its not.
     */
    public boolean isAcceptingPlayers();

    /**
     * Waiting for a new player socket to connects
     * @param serverSocket is the socket of the player
     */
    public void acceptPlayer(Socket serverSocket);

    /**
     * Removes the game from server list
     */
    public void removeFromServerList();
}
