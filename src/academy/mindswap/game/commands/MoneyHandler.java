/*
 * @(#)MoneyHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.game.commands;

import static academy.mindswap.game.messages.GameMessages.*;

import academy.mindswap.game.Game;
import java.util.Arrays;

/**
 * If the spin lands money the player will have the opportunity to choose three options and than receive the money
 */
public class MoneyHandler implements CommandHandler {

    private static final int VOWELPRICE = 2000;
    private final int bonus;
    private Game game;
    private Game.PlayerHandler playerHandler;

    /**
     * Method constructor that receive one argument
     * @param bonus refers to the money that player can win
     */
    public MoneyHandler(int bonus) {
        this.bonus = bonus;
    }

    /**
     * Execute the methods of money handler
     * Check if the player have enough money to buy a vowel, and change the local variable if don't have money
     * Send message for all the players to inform what is append in the game
     * Update the local variable with the method get player answer
     * In switch we invoke the method resultant of the option from player
     * In every option the player can guess the quote
     * Send message for all the players to inform what letter the players chose
     * @param game represent the instance of a member class game
     * @param playerHandler o access the properties and methods of player
     * @throws NullPointerException when player closes the socket on this side
     */
    @Override
    public void execute(Game game, Game.PlayerHandler playerHandler) throws NullPointerException{
        this.game = game;
        this.playerHandler = playerHandler;
        String optionsRegex = "[abc]"; // Only accept the words in square brackets from player
        String message = MONEY_OPTIONS;
        String option;
        if(playerHandler.getPlayerCash()<VOWELPRICE){
            optionsRegex = "[ab]";
            message= NO_MONEY_BUY_VOWEL;
        }
        game.broadcast(String.format (WAITING_PLAYER_OPTION,playerHandler.getName()),playerHandler);
        option = getPlayerAnswer(message, optionsRegex, playerHandler.getName() + INVALID_OPTION);
        if (option==null){      //occurs when suddenly a player closes client
            return;
        }
        switch (option) {
            case "a":
                game.broadcast(String.format (PLAYER_CHOOSE_A_CONSONANT,playerHandler.getName()),playerHandler);//send
                // a personalized message to the player is playing in the moment
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

    /**
     * Will waits for a message from the player client
     * @return return null when player socket is close in otherwise do a to lower case from the answer
     */
    private String getMessageFromBuffer(){
        String answer=playerHandler.getAnswer();
        return answer!=null? answer.toLowerCase(): null;
    }

    /**
     * Received a answer from player
     * Until the answer is valid or not null the player will answer again
     * @param messageToSend represent the option that player have to chose
     * @param regex represent the word that regex will accept
     * @param invalidMessage represent the name and the INVALID_OPTION
     * @return string with the answer of player
     */
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

    /**
     * Verify if client answer with one character and with the regex defined before
     * @param playerAnswer represent the answer of the player
     * @param regex represent the word that regex will accept
     * @return true if the answer is valid to play otherwise return false
     */
    private boolean validateAnswer(String playerAnswer, String regex) {
        if(playerAnswer==null){ //occurs when suddenly a player closes client
            return false;
        }
        if (playerAnswer.length() != 1) {
            return false;
        }
        return playerAnswer.toLowerCase().matches(regex);
    }

    /**
     * Check if the letter the player send exist in quote
     * In case the letter is wrong send message to inform the fail answer
     * @param letter represent the letter, consonant or vowel, form the player
     */
    private void playerGuessedLetter(String letter){

        if(game.getQuoteToGuess().toLowerCase().contains(letter)) {
            playerHandler.addCash(bonus);
            game.broadcast(String.format(WON_BONUS,playerHandler.getName(),bonus, letter));
            guessQuoteFlow();
            return;
        }

        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),letter));
    }

    /**
     * Ask a player to pick an consonant
     * Add the consonant that player send to the letters array
     * Check if the consonant exist in quote
     */
    private void consonantFlow() {
        String consonantRegex = "[a-z&&[^aeiou]]"; //Only accept the words from a-z exclude the vowels
        String consonant;

        consonant=getPlayerAnswer(CHOOSE_A_CONSONANT, consonantRegex, INVALID_CONSONANT);
        game.addPlayerLetters(consonant);
        playerGuessedLetter(consonant);
    }

    /**
     * Ask a player to buy a vowel
     * Remove from player cash the price of the vowel
     * Add the consonant that player send to the letters array
     * Check if the vowel exist in quote
     */
    private void vowelFlow() {
        String vowelRegex = "[aeiou]"; //Only accept vowels
        String vowel;
        vowel=getPlayerAnswer(CHOOSE_A_VOWEL, vowelRegex, INVALID_VOWEL);

        playerHandler.removeCash(VOWELPRICE);
        game.addPlayerLetters(vowel);
        playerGuessedLetter(vowel);
    }

    /**
     * Ask a player to guess the quote
     * Send the player the quote to guess
     * Check if the quote sent by player is correct
     * Send to all players if the player guess the quote or not
     * If the player have guess the quote the game will end for all the players
     */
    private void guessQuoteFlow() {

        playerHandler.send(game.drawBoard());
        playerHandler.send(GUESS_QUOTE);
        String answer;
        answer=getMessageFromBuffer();
        if(answer==null){ //occurs when suddenly a player closes client
            return;
        }
        if (checkIfPlayerGuessedQuote(answer)) {
            Arrays.stream(game.getQuoteToGuess().split("")).forEach(game::addPlayerLetters);
            game.broadcast(String.format(PLAYER_WON,playerHandler.getName(),playerHandler.getPlayerCash(),answer));
            game.endGame();
            return;
        }
        game.broadcast(String.format(FAIL_ANSWER,playerHandler.getName(),answer) );
    }


    private boolean checkIfPlayerGuessedQuote(String answer){
          return  answer
                  .toLowerCase()
                  .replaceAll("[^a-z]","")
                  .equals(game
                          .getQuoteToGuess()
                          .toLowerCase()
                          .replaceAll("[^a-z]",""));
    }



}
