package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

/**
 * If the spin lands Free play the player can play twice
 */

public class FreePlayHandler implements CommandHandler{

    private CommandHandler MoneyHandler;

    /**
     *
     * @param game represent the instance of a member class game
     * @param playerHandler to access the properties and methods of player
     */
    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) {
        CommandHandler command = MoneyHandler;
        command.execute(game, playerHandler);
        command.execute(game,playerHandler);
    }
}
