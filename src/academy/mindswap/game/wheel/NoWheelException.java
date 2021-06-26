package academy.mindswap.game.wheel;

public class NoWheelException extends Exception{
    public NoWheelException() {
        super("You need to create a wheel first");
    }
}
