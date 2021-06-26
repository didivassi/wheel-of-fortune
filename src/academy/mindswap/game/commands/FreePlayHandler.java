package academy.mindswap.game.commands;

import academy.mindswap.game.Game;
import static academy.mindswap.game.messages.GameMessages.*;

/**
 * If the spin lands Free play the player can play twice
 */
public class FreePlayHandler implements CommandHandler{
    /**
     * The command Money will inkove twice for player have a opportunity to play twice
     * Send a message to player
     * @param game represent the instance of a member class game
     * @param playerHandler to access the properties and methods of player
     */
    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) throws NullPointerException{
        CommandHandler command = new MoneyHandler(0);
        game.broadcast(String.format(FREE_PLAY,playerHandler.getName()));
        command.execute(game, playerHandler);
        command.execute(game,playerHandler);
    }
}
