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
     * @throws NullPointerException
     */
    void execute (Game game, Game.PlayerHandler playerHandler) throws NullPointerException;
}
