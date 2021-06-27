/*
 * @(#)CommandHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.commands;



import academy.mindswap.games_factory.wheel_of_fortune.WheelOfFortune;

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
    void execute (WheelOfFortune game, WheelOfFortune.PlayerHandler playerHandler) throws NullPointerException;
}
