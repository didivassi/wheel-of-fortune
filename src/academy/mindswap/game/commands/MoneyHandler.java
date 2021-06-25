package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

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
        if(game.getQuoteToGuess().contains(consonant)) {
            playerHandler.addCash(bonus);
        }

    }

    private void vowelFlow(Game game, Game.PlayerHandler playerHandler) {
        String vowelRegex = "[aeiou]";
        playerHandler.send(CHOOSE_A_VOWEL);

        String option = playerHandler.getAnswer().toLowerCase();

        while (!checkAnswer(option, vowelRegex)) {
            playerHandler.send(playerHandler.getName() + INVALID_VOWEL);
            option = playerHandler.getAnswer().toLowerCase();
        }

    }

    private void guessQuoteFlow(Game game, Game.PlayerHandler playerHandler) {


    }



}
