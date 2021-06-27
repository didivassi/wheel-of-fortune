package academy.mindswap.games.factory.wheel_of_fortune.ascii_art;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board {

    public static void main(String[] args) {
        String quote="You catch more flies with honey than with vinegar.";
        Set<String> letters = new HashSet<>(
                Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"));
        System.out.println(drawBoard(quote,letters));

    }

    public  static  String  title(){
       return " __        ___               _          __   _____          _                    \n" +
                " \\ \\      / / |__   ___  ___| |   ___  / _| |  ___|__  _ __| |_ _   _ _ __   ___ \n" +
                "  \\ \\ /\\ / /| '_ \\ / _ \\/ _ \\ |  / _ \\| |_  | |_ / _ \\| '__| __| | | | '_ \\ / _ \\\n" +
                "   \\ V  V / | | | |  __/  __/ | | (_) |  _| |  _| (_) | |  | |_| |_| | | | |  __/\n" +
                "    \\_/\\_/  |_| |_|\\___|\\___|_|  \\___/|_|   |_|  \\___/|_|   \\__|\\__,_|_| |_|\\___|\n";

    }

    public static String  drawBoard(String quote, Set<String> letters){
        String quoteWhiteSpaces = "                                                          ";
        String pWhiteSpaces = "                  ";
        String[] player1={"Diogo","1000€"};
        String[] player2={"Filipa","2000€"};
        String[] player3={"Manuela","3000€"};
        String[] splicedQuote = splitStringRows(quote);
        String lettersTaken= "Chosen:".concat(String.join(",",letters));
        return          title()+
                        "   _,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,_\n"+
                        "       __   __________________________________________________________   __\n"+
                        "         | |                                                          | |\n"+
                        "         | |"+centerInWhiteSpaces(quoteWhiteSpaces,splicedQuote[0])+"| |\n"+
                        "         | |"+centerInWhiteSpaces(quoteWhiteSpaces,splicedQuote[1])+"| |\n"+
                        "         | |                                                          | |\n"+
                        "       __| |__________________________________________________________| |__\n"+
                        "       __   __________________________________________________________   __\n"+
                        "         | |"+alignLeftInWhiteSpaces(quoteWhiteSpaces,lettersTaken)+"| |\n"+
                        "       __| |__________________________________________________________| |__\n"+
                "         | |"+centerInWhiteSpaces(pWhiteSpaces,player1[0]) +"||"+centerInWhiteSpaces(pWhiteSpaces,player2[0]) +
                        "||"+centerInWhiteSpaces(pWhiteSpaces,player3[0]) +"| |  \n"+
                "         | |"+centerInWhiteSpaces(pWhiteSpaces,player1[1]) +"||"+centerInWhiteSpaces(pWhiteSpaces,player2[1]) +
                "||"+centerInWhiteSpaces(pWhiteSpaces,player3[1]) +"| |  \n"+
                        "       __| |__________________||__________________||__________________| |__\n";

    }

    private static String[] splitStringRows(String quote){
        String[] wordsArray=quote.split(" ");
        int len = wordsArray.length;
        String firstHalf = String.join(" ", Arrays.copyOfRange(wordsArray, 0, len / 2));
        String secondHalf = String.join(" ", Arrays.copyOfRange(wordsArray, (len / 2), len));

        return new String[]{firstHalf, secondHalf};

    }

    private static String centerInWhiteSpaces(String whiteSpaces,String toCenter ){
        int startingIndex;
        final int i = (whiteSpaces.length() - toCenter.length()) / 2;
        if(toCenter.length()%2==0){
            startingIndex= i;
        }else {
            startingIndex= i +1;
        }
        return whiteSpaces
                .substring(0,startingIndex)
                .concat(toCenter)
                .concat(whiteSpaces.substring(startingIndex+toCenter.length()));
    }

    private static String alignLeftInWhiteSpaces(String whiteSpaces,String toLeft ){
        return toLeft.concat(whiteSpaces
                .substring(toLeft.length()));

    }
}
