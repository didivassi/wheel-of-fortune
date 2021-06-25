package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

public class MoneyHandler implements CommandHandler{

    private int bonus;

    public MoneyHandler (int bonus) {
        this.bonus = bonus;
    }

    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) {

    }

    //PlayerHandler.send(OPTIONS_TO_PLAY);
    //Switch(Game.PlayerHandler.getAnswer()){
    //    case
    //}

}
