package academy.mindswap.game.commands;


public enum Command {
    BANKRUPT("Bankrupt", new BankruptHandler()),
    FREE_PLAY("Free Play", new FreeplayHandler()),
    MISS_TURN("Miss Turn", new MissTurnHandler()),
    ONE_THOUSAND("Money", new MoneyHandler(1000)),
    FIVE_HUNDRED("500", new MoneyHandler(500)),
    THREE_HUNDRED("300", new MoneyHandler(300)),
    ONE_HUNDRED("100", new MoneyHandler(100)),
    FIFTY("50", new MoneyHandler(50));

    private String description;
    private CommandHandler handler;

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public CommandHandler getHandler () {
        return handler;
    }

}
