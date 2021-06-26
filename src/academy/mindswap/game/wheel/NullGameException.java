package academy.mindswap.game.wheel;

public class NullGameException extends Exception{
    public NullGameException() {
        super("The provided game object is null");
    }
}
