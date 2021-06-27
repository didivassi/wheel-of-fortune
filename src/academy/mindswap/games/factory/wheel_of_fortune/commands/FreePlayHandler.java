/*
 * @(#)FreePlayHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory.wheel_of_fortune.commands;

import static academy.mindswap.games.factory.wheel_of_fortune.messages.GameMessages.*;

import academy.mindswap.games.factory.wheel_of_fortune.WheelOfFortune;


/**
 * If the spin lands Free play the player can play twice
 */
public class FreePlayHandler implements CommandHandler{
    /**
     * The command Money will inkove twice for player have a opportunity to play twice
     * Send a message to player
     * @param game represent the instance of a member class game
     * @param playerHandler to access the properties and methods of player
     * @throws NullPointerException when player closes the socket on this side
     */
    @Override
    public void execute(WheelOfFortune game, WheelOfFortune.PlayerHandler playerHandler) throws NullPointerException{
        CommandHandler command = new MoneyHandler(0);
        game.broadcast(String.format(FREE_PLAY,playerHandler.getName()));
        command.execute(game, playerHandler);
        command.execute(game,playerHandler);
    }
}
