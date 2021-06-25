package academy.mindswap.game;

import java.util.*;
import java.util.stream.Collectors;

import static academy.mindswap.messages.Messages.FAIL_ANSWER;
import static academy.mindswap.messages.Messages.GUESS_QUOTE;

public class Temp {
    public static void main(String[] args) {
        System.out.println(new Temp().guessQuoteFlow());

        }


    public boolean guessQuoteFlow() {

        String quote="Don’t judge a book by its cover.";
        String answer="dont judge a book by its cover";
        System.out.println(answer
                .toLowerCase()
                .replaceAll("[^a-z]",""));
        System.out.println(quote
                .toLowerCase()
                .replaceAll("[^a-z]",""));
       return  answer
                .toLowerCase()
                .replaceAll("[^a-z]","")
                .equals(quote
                        .toLowerCase()
                        .replaceAll("[^a-z]",""));



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
