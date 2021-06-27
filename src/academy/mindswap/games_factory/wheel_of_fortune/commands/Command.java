/*
 * @(#)Command.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.commands;

/**
 * Commands that are available to be used by the wheel in each game
 */

public enum Command {
    BANKRUPT("Bankrupt", new BankruptHandler(),true),
    FREE_PLAY("Free Play", new FreePlayHandler(),true),
    MISS_TURN("Miss Turn", new MissTurnHandler(),true),
    ONE_THOUSAND("1000", new MoneyHandler(1000),false),
    FIVE_HUNDRED("500", new MoneyHandler(500),false),
    THREE_HUNDRED("300", new MoneyHandler(300),false),
    ONE_HUNDRED("100", new MoneyHandler(100),false),
    FIFTY("50", new MoneyHandler(50),false);

    private String description;
    private CommandHandler handler;
    private boolean isPenalty;

    /**
     * Method constructor of the enum command this accept three arguments
     * @param description represents the name of the command
     * @param handler represents the command that will receive and need to be handler
     * @param isPenalty represents if player will received money
     */
    Command(String description, CommandHandler handler, boolean isPenalty) {
        this.description = description;
        this.handler = handler;
        this.isPenalty=isPenalty;

    }

    /**
     * Allows to know what command that will be used
     * @return the enum command
     */
    public CommandHandler getHandler () {
        return handler;
    }

    /**
     * Verify if the isPenalty is true and the player doesn't received money or if false he receives money to player
     * @return returns true or false that represents the penalty of command
     */
    public boolean isPenalty() {
        return isPenalty;
    }

    /**
     *
     * @return the description of the command when is instanced
     */
    @Override
    public String toString(){
        return description;
    }

}
