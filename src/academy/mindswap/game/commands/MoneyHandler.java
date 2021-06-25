package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

import java.util.Arrays;

import static academy.mindswap.messages.Messages.*;

public class MoneyHandler implements CommandHandler {

    private int bonus;
    private Game game;
    private Game.PlayerHandler playerHandler;

    public MoneyHandler(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) {
        this.game=game;
        this.playerHandler=playerHandler;
        String optionsRegex = "[abc]";
        String message=MONEY_OPTIONS;

        String option;
        if(playerHandler.getPlayerCash()<3000){
            optionsRegex = "[ab]";
            message= MONEY_OPTIONS;
        }

        option = getPlayerAnswer(message, optionsRegex, playerHandler.getName() + INVALID_OPTION);
        switch (option) {
            case "a":
                consonantFlow(game, playerHandler);
                break;
            case "b":
                guessQuoteFlow(game, playerHandler);
                break;
            default:
                vowelFlow(game, playerHandler);
                break;
        }
        game.broadcast(CHOSEN_LETTERS+ "["+game.getListOfChosenLetters()+"]\n");
        game.broadcast(game.prepareQuoteToGame());

    }

    private boolean checkAnswer(String playerAnswer, String regex) {
        if (playerAnswer.length() != 1) {
            return false;
        }
        return playerAnswer.toLowerCase().matches(regex);
    }

    private String getMessageFromBuffer(){
        try {
            return playerHandler.getAnswer().toLowerCase();
        }catch (NullPointerException e){
            return "";
        }
    }

    private String getPlayerAnswer(String messageToSend, String regex, String invalidMessage){
        playerHandler.send(messageToSend);
        String answer;
        answer=getMessageFromBuffer();
        while (!checkAnswer(answer, regex)  && !playerHandler.hasLef()) {
            playerHandler.send(playerHandler.getName() + invalidMessage);
            answer = getMessageFromBuffer();
        }
        return answer;
    }

    private void quoteContainsLetter(String letter){
        if(game.getQuoteToGuess().toLowerCase().contains(letter)) {
            playerHandler.addCash(bonus);
            game.broadcast(String.format(WON_BONUS,playerHandler.getName(),bonus, letter));
            guessQuoteFlow(game, playerHandler);
            return;
        }
        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),letter));
    }

    private void consonantFlow(Game game, Game.PlayerHandler playerHandler) {
        String consonantRegex = "[a-z&&[^aeiou]]";
        String consonant;

        consonant=getPlayerAnswer(CHOOSE_A_CONSONANT, consonantRegex, INVALID_CONSONANT);
        game.addPlayerLetters(consonant);
        quoteContainsLetter(consonant);
    }

    private void vowelFlow(Game game, Game.PlayerHandler playerHandler) {
        String vowelRegex = "[aeiou]";
        String vowel;
        vowel=getPlayerAnswer(CHOOSE_A_VOWEL, vowelRegex, INVALID_VOWEL);

        playerHandler.removeCash(3000);
        game.addPlayerLetters(vowel);
        quoteContainsLetter(vowel);
    }

    private void guessQuoteFlow(Game game, Game.PlayerHandler playerHandler) {
        playerHandler.send(GUESS_QUOTE);
        String answer;
        answer=getMessageFromBuffer();
        if (answer
                .toLowerCase()
                .replaceAll("[^a-z]","")
                .equals(game
                        .getQuoteToGuess()
                        .toLowerCase()
                        .replaceAll("[^a-z]",""))) {
            Arrays.stream(game.getQuoteToGuess().split("")).forEach(game::addPlayerLetters);
            game.broadcast(String.format(PLAYER_WON,playerHandler.getName(),answer));
            game.endGame();
            return;
        }
        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),answer) );
    }

}
