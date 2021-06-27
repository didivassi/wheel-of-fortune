package academy.mindswap.game.ascii_art;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {

    public static void main(String[] args) {
        String quote="An apple a day keeps the doctor away";
        Set<String> letters = new HashSet<>(
                Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","m","n","o","p","q","r","s"));
        System.out.println(drawBoard(quote,letters));

    }

    public static String  drawBoard(String quote, Set<String> letters){
        String whiteSpaces = "                                            ";
        String[] splicedQuote = splitStringRows(quote);
        String lettersTaken= "Chosen:".concat(String.join(",",letters));

        return         "__| |____________________________________________| |__\n"+
                        "__   ____________________________________________   __\n"+
                        "  | |                                            | |\n"+
                        "  | |"+centerInWhiteSpaces(whiteSpaces,splicedQuote[0])+"| |\n"+
                        "  | |"+centerInWhiteSpaces(whiteSpaces,splicedQuote[1])+"| |\n"+
                        "  | |                                            | |\n"+
                        "__| |____________________________________________| |__\n"+
                        "__   ____________________________________________   __\n"+
                        "  | |"+centerInWhiteSpaces(whiteSpaces,lettersTaken)+"| |\n";

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


}
