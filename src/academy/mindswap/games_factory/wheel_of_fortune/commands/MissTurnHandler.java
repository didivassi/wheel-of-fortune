/*
 * @(#)MissTurnHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.commands;

import academy.mindswap.games_factory.wheel_of_fortune.WheelOfFortune;
import academy.mindswap.games_factory.wheel_of_fortune.messages.GameMessages;

/**
 * If the spin lands miss turn the player will not play and the game move to the next player
 */

public class MissTurnHandler implements CommandHandler{
    /**
     * Send a message to player
     * @param game represent the instance of a member class game
     * @param playerHandler to access the properties and methods of player
     */
    @Override
    public void execute(WheelOfFortune game, WheelOfFortune.PlayerHandler playerHandler) {
        game.broadcast(String.format(GameMessages.MISS_TURN,playerHandler.getName()));
    }
}
