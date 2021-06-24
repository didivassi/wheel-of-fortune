package academy.mindswap.game.commands;

import org.omg.CORBA.PRIVATE_MEMBER;

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
