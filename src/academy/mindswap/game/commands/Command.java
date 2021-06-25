package academy.mindswap.game.commands;


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

    Command(String description, CommandHandler handler, boolean isPenalty) {
        this.description = description;
        this.handler = handler;
        this.isPenalty=isPenalty;

    }

    public CommandHandler getHandler () {
        return handler;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    @Override
    public String toString(){
        return description;
    }

}
