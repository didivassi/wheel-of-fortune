package academy.mindswap.game.commands;

import academy.mindswap.game.Game;
import static academy.mindswap.messages.Messages.*;

public class MissTurnHandler implements CommandHandler{
    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) {
        playerHandler.send(MISS_TURN_RESULT);
    }
}
