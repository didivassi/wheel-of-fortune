/*
 * @(#)Server.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

/**
 * All the commands implement the interface command handler
 */

public interface CommandHandler {
    /**
     *This method represent the action that each player will have
     * @param game represent the instance of a member class game
     * @param playerHandler o access the properties and methods of player
     * @throws NullPointerException when player closes the socket on this side
     */
    void execute (Game game, Game.PlayerHandler playerHandler) throws NullPointerException;
}
