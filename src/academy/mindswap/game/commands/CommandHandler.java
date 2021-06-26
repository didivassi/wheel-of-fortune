package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

public interface CommandHandler {
    void execute (Game game, Game.PlayerHandler playerHandler) throws NullPointerException;
}
