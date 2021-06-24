package academy.mindswap.game;

public class ConsoleHelper {
    private String lastLine = "";

    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    private byte anim;

    public void animate(String line) {
        switch (anim) {
            case 1:
                print("[ Money ] " + line);
                break;
            case 2:
                print("[ Bankrupt ] " + line);
                break;
            case 3:
                print("[ Freeplay ] " + line);
                break;
            default:
                anim = 0;
                print("[ Miss turn ] " + line);
        }
        anim++;
    }

    public static void main(String[] args) throws InterruptedException {
        ConsoleHelper consoleHelper = new ConsoleHelper();
        for (int i = 0; i < 20; i++) {
            consoleHelper.animate(  "spinning wheel");
            //simulate a piece of task
            Thread.sleep(400);
        }
        System.out.println("\r[ finished ] wheel says");
    }
}

