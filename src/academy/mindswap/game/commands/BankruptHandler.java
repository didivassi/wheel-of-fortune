package academy.mindswap.game.commands;

import academy.mindswap.game.Game;
import static academy.mindswap.messages.Messages.*;

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
    public void execute(Game game, Game.PlayerHandler playerHandler) {
        playerHandler.removeCash(playerHandler.getPlayerCash());
        game.broadcast(String.format(BANKRUPT,playerHandler.getName()));
    }
}
