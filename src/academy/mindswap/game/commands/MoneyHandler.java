package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

import java.util.Arrays;

import static academy.mindswap.messages.Messages.*;

public class MoneyHandler implements CommandHandler {

    private int bonus;

    public MoneyHandler(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) {
        playerHandler.send(MONEY_OPTIONS);

        String optionsRegex = "[abc]";

        String option = playerHandler.getAnswer().toLowerCase();

        while (!checkAnswer(option, optionsRegex)) {
            playerHandler.send(playerHandler.getName() + INVALID_OPTION);
            option = playerHandler.getAnswer().toLowerCase();
        }

        switch (option) {
            case "a":
                consonantFlow(game, playerHandler);
                break;
            case "b":
                vowelFlow(game, playerHandler);
                break;
            default:
                guessQuoteFlow(game, playerHandler);
                break;
        }

        game.broadcast(CHOSEN_LETTERS + game.getListOfChosenLetters());
        game.broadcast(game.prepareQuoteToGame());

    }

    private boolean checkAnswer(String playerAnswer, String regex) {
        if (playerAnswer.length() != 1) {
            return false;
        }
        return playerAnswer.toLowerCase().matches(regex);
    }

    private void consonantFlow(Game game, Game.PlayerHandler playerHandler) {
        String consonantRegex = "[a-z&&[^aeiou]]";
        playerHandler.send(CHOOSE_A_CONSONANT);
        String consonant = playerHandler.getAnswer().toLowerCase();

        while (!checkAnswer(consonant, consonantRegex)) {
            playerHandler.send(playerHandler.getName() + INVALID_CONSONANT);
            consonant = playerHandler.getAnswer().toLowerCase();
        }
        game.addPlayerLetters(consonant);
        if(game.getQuoteToGuess().contains(consonant)) {
            playerHandler.addCash(bonus);
            game.broadcast(String.format(WON_BONUS,playerHandler.getName(),bonus, consonant));
            guessQuoteFlow(game, playerHandler);
        }
        game.broadcast(FAIL_ANSWER);
    }

    private void vowelFlow(Game game, Game.PlayerHandler playerHandler) {
        String vowelRegex = "[aeiou]";
        playerHandler.send(CHOOSE_A_VOWEL);
        String vowel = playerHandler.getAnswer().toLowerCase();
        playerHandler.removeCash(3000);
        while (!checkAnswer(vowel, vowelRegex)) {
            playerHandler.send(playerHandler.getName() + INVALID_VOWEL);
            vowel = playerHandler.getAnswer().toLowerCase();
        }
        game.addPlayerLetters(vowel);
        if(game.getQuoteToGuess().contains(vowel)) {
            playerHandler.addCash(bonus);
            game.broadcast(String.format(WON_BONUS,playerHandler.getName(),bonus, vowel));
            guessQuoteFlow(game, playerHandler);
        }
        game.broadcast(FAIL_ANSWER);
    }

    private void guessQuoteFlow(Game game, Game.PlayerHandler playerHandler) {
        playerHandler.send(GUESS_QUOTE);
        if (playerHandler.getAnswer()
                .toLowerCase()
                .replace("[^a-z]","")
                .equals(game
                        .getQuoteToGuess()
                        .replace("[^a-z]",""))) {
            Arrays.stream(game.getQuoteToGuess().split("")).forEach(game::addPlayerLetters);
        }
        game.broadcast(FAIL_ANSWER);
    }

}
