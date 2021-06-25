package academy.mindswap.game.commands;


public enum Command {
    MONEY,
    FREEPLAY,
    MISSTURN,
    BANKRUPT;

    private String description;
    private CommandHandler handler;

    Command() {

    }




}
