package academy.mindswap.game.commands;

import academy.mindswap.game.Game;

import java.util.Arrays;

import static academy.mindswap.game.messages.GameMessages.*;

public class MoneyHandler implements CommandHandler {

    private final int bonus;
    private Game game;
    private Game.PlayerHandler playerHandler;

    public MoneyHandler(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) throws NullPointerException{
        this.game=game;
        this.playerHandler=playerHandler;
        String optionsRegex = "[abc]";
        String message=MONEY_OPTIONS;
        String option;
        if(playerHandler.getPlayerCash()<3000){
            optionsRegex = "[ab]";
            message= NO_MONEY_BUY_VOWEL;
        }
        game.broadcast(String.format (WAITING_PLAYER_OPTION,playerHandler.getName()),playerHandler);
        option = getPlayerAnswer(message, optionsRegex, playerHandler.getName() + INVALID_OPTION);
        if (option==null){  //occurs when suddenly a player closes client
            return;
        }
        switch (option) {
            case "a":
                game.broadcast(String.format (PLAYER_CHOOSE_A_CONSONANT,playerHandler.getName()),playerHandler);
                consonantFlow();
                break;
            case "b":
                game.broadcast(String.format(PLAYER_CHOOSE_GUESS,playerHandler.getName()),playerHandler);
                guessQuoteFlow();
                break;
            default:
                game.broadcast(String.format(PLAYER_CHOOSE_A_VOWEL,playerHandler.getName()),playerHandler);
                vowelFlow();
                break;
        }

        game.broadcast(String.format(CHOSEN_LETTERS,game.getListOfChosenLetters()));

    }

    private String getMessageFromBuffer(){
        String answer=playerHandler.getAnswer();
        return answer!=null? answer.toLowerCase(): null;
    }

    private String getPlayerAnswer(String messageToSend, String regex, String invalidMessage){
        playerHandler.send(messageToSend);
        String answer;
        answer=getMessageFromBuffer();
        while (!validateAnswer(answer, regex)  && answer!=null) {
            playerHandler.send(playerHandler.getName() + invalidMessage);
            answer = getMessageFromBuffer();
        }
        return answer;
    }

    private boolean validateAnswer(String playerAnswer, String regex) {
        if(playerAnswer==null){ //occurs when suddenly a player closes client
            return false;
        }
        if (playerAnswer.length() != 1) {
            return false;
        }
        return playerAnswer.toLowerCase().matches(regex);
    }

    private void playerGuessedLetter(String letter){

        if(game.getQuoteToGuess().toLowerCase().contains(letter)) {
            playerHandler.addCash(bonus);
            game.broadcast(String.format(WON_BONUS,playerHandler.getName(),bonus, letter));
            guessQuoteFlow();
            return;
        }

        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),letter));
    }

    private void consonantFlow() {
        String consonantRegex = "[a-z&&[^aeiou]]";
        String consonant;

        consonant=getPlayerAnswer(CHOOSE_A_CONSONANT, consonantRegex, INVALID_CONSONANT);
        game.addPlayerLetters(consonant);
        playerGuessedLetter(consonant);
    }

    private void vowelFlow() {
        String vowelRegex = "[aeiou]";
        String vowel;
        vowel=getPlayerAnswer(CHOOSE_A_VOWEL, vowelRegex, INVALID_VOWEL);

        playerHandler.removeCash(3000);
        game.addPlayerLetters(vowel);
        playerGuessedLetter(vowel);
    }

    private void guessQuoteFlow() {
        playerHandler.send(GUESS_QUOTE);
        playerHandler.send(game.prepareQuoteToGame());
        String answer;
        answer=getMessageFromBuffer();
        if(answer==null){
            return;
        }
        if (answer
                .toLowerCase()
                .replaceAll("[^a-z]","")
                .equals(game
                        .getQuoteToGuess()
                        .toLowerCase()
                        .replaceAll("[^a-z]",""))) {
            Arrays.stream(game.getQuoteToGuess().split("")).forEach(game::addPlayerLetters);
            game.broadcast(String.format(PLAYER_WON,playerHandler.getName(),playerHandler.getPlayerCash(),answer));
            game.endGame();
            return;
        }
        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),answer) );
    }

}
