/*
 * @(#)BankruptHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory.wheel_of_fortune.commands;

import static academy.mindswap.games.factory.wheel_of_fortune.messages.GameMessages.*;


import academy.mindswap.games.factory.Game;
import academy.mindswap.games.factory.wheel_of_fortune.WheelOfFortune;

/**
 * If the spin lands bankrupt the player lose all the money that have in cash and don´t play
 */
public class BankruptHandler implements CommandHandler{
    /**
     * Goes to the player cash and remove all the money
     * Send a message to player
     * @param game represent the instance of a member class game
     * @param playerHandler to access the properties and methods of player
     */
    @Override
    public void execute(WheelOfFortune game, WheelOfFortune.PlayerHandler playerHandler) {
        playerHandler.removeCash(playerHandler.getPlayerCash());
        game.broadcast(String.format(BANKRUPT,playerHandler.getName()));
    }
}
