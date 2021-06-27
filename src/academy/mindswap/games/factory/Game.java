/*
 * @(#)Game.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory;

import academy.mindswap.games.factory.GameController;
import academy.mindswap.games.factory.GameType;
import academy.mindswap.server.Server;

/**
 * Game is a abstract class that implements the interface game controller
 * Every game type extends from this class
 */
public abstract class Game implements GameController {

    private Server server;
    private GameType gameType;

    /**
     *
     * @param server is a instanced of server class
     * @param gameType is a instanced of a game type
     */
    public Game(Server server, GameType gameType){
        this.server = server;
        this.gameType = gameType;
    }

    /**
     * Removes the game from server list
     */
    public void removeFromServerList() {
        server.removeGameFromList(this);
    }



}
