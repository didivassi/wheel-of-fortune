package academy.mindswap.games.factory.wheel_of_fortune.wheel;

public class NullGameException extends Exception{
    public NullGameException() {
        super("The provided game object is null");
    }
}
