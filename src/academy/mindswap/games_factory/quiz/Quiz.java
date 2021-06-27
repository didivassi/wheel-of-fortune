/*
 * @(#)Quiz.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.quiz;

import academy.mindswap.games_factory.Game;
import academy.mindswap.games_factory.GameType;
import academy.mindswap.server.Server;

import java.net.Socket;

/**
 * Quiz is a game type and a subclass of the class game
 * Implements the game logic
 */
public class Quiz extends Game {

    /**
     * Constructor method
     * @param server is a instanced of server class
     */
    public Quiz(Server server){
        super(server, GameType.QUIZ);
    }

    /**
     * Verify if the game is accepting players.
     * @return returns true if there is a game that can accept a player false if its not.
     */
    @Override
    public boolean isAcceptingPlayers() {
        return false;
    }


    /**
     * A new player connects to the game and sends it to the execution service as a new thread
     *
     * @param playerSocket the socket from the client that connected
     */
    @Override
    public void acceptPlayer(Socket playerSocket) {

    }

    @Override
    public void run() {

    }
}
