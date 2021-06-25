package academy.mindswap.game;

public enum WheelOptions {
    BANKRUPT("Bankrupt",true),
    FREE_PLAY("Free Play",true),
    MISS_TURN("Miss Turn",true),
    ONE_THOUSAND("1000",false),
    FIVE_HUNDRED("500",false),
    THREE_HUNDRED("300",false),
    ONE_HUNDRED("100",false),
    FIFTY("50",false);

    private String description;
    private boolean isPenalty;

    WheelOptions(String description,boolean isPenalty){
        this.description=description;
        this.isPenalty=isPenalty;

    }

    public boolean isPenalty() {
        return isPenalty;
    }

    @Override
    public String toString(){
        return description;
    }


}
