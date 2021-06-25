package academy.mindswap.game;

import java.util.*;
import java.util.stream.Collectors;

public class Temp {
    public static void main(String[] args) {
        System.out.println(new Temp().prepareQuoteToGame2());

        }

    public String prepareQuoteToGame() {
        String quoteToGuess = "If it ain’t broke, don’t fix it.";
        List<String> playerLetters = new LinkedList<>();
        playerLetters.add( " ");
        playerLetters.add("o");
        playerLetters.add("b");
        playerLetters.add("i");
        return Arrays.stream(quoteToGuess.split(""))
                .map(c -> c = playerLetters.contains(c.toLowerCase()) ? c : "#")
                .collect(Collectors.joining());

    }

    public String prepareQuoteToGame2() {
        String quoteToGuess = "If it ain’t broke, don’t fix it.";
        List<String> playerLetters = new LinkedList<>();
        playerLetters.add("o");
        playerLetters.add("b");
        playerLetters.add("i");
        String regex= playerLetters.stream().collect(Collectors.joining());
        System.out.println(regex);
        return Arrays.stream(quoteToGuess.split(""))
                .map(c -> c =c.toLowerCase().matches("["+regex+"||[^a-z]]") ? c : "#")
                .collect(Collectors.joining());

    }


}
