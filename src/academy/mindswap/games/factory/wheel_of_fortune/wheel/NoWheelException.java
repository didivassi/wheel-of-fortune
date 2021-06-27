package academy.mindswap.games.factory.wheel_of_fortune.wheel;

public class NoWheelException extends Exception{
    public NoWheelException() {
        super("You need to create a wheel first");
    }
}
