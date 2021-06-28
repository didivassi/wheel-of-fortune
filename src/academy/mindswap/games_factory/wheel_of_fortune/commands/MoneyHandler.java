/*
 * @(#)MoneyHandler.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.commands;

import static academy.mindswap.games_factory.wheel_of_fortune.ascii_art.BigWinner.*;
import academy.mindswap.games_factory.wheel_of_fortune.WheelOfFortune;
import academy.mindswap.games_factory.wheel_of_fortune.messages.GameMessages;

import java.util.Arrays;

/**
 * If the spin lands money the player will have the opportunity to choose three options and than receive the money
 */
public class MoneyHandler implements CommandHandler {

    private static final int VOWEL_PRICE = 2000;
    private static final int WINNER_BONUS = 10000;
    private final int bonus;
    private WheelOfFortune game;
    private WheelOfFortune.PlayerHandler playerHandler;

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
    public void execute(WheelOfFortune game, WheelOfFortune.PlayerHandler playerHandler) throws NullPointerException{
        this.game = game;
        this.playerHandler = playerHandler;
        String optionsRegex = "[abc]"; // Only accept the words in square brackets from player
        String message = GameMessages.MONEY_OPTIONS;
        String option;
        if(playerHandler.getPlayerCash()<VOWEL_PRICE){
            optionsRegex = "[ab]";
            message= GameMessages.NO_MONEY_BUY_VOWEL;
        }
        game.broadcast(String.format (GameMessages.WAITING_PLAYER_OPTION,playerHandler.getName()),playerHandler);
        option = getPlayerAnswer(message, optionsRegex, playerHandler.getName() + GameMessages.INVALID_OPTION);
        if (option==null){      //occurs when suddenly a player closes client
            return;
        }
        switch (option) {
            case "a":
                game.broadcast(String.format (GameMessages.PLAYER_CHOOSE_A_CONSONANT,playerHandler.getName()),playerHandler);//send
                // a personalized message to the player is playing in the moment
                consonantFlow();
                break;
            case "b":
                game.broadcast(String.format(GameMessages.PLAYER_CHOOSE_GUESS,playerHandler.getName()),playerHandler);
                guessQuoteFlow();
                break;
            default:
                game.broadcast(String.format(GameMessages.PLAYER_CHOOSE_A_VOWEL,playerHandler.getName()),playerHandler);
                vowelFlow();
                break;
        }
        game.broadcast(String.format(GameMessages.CHOSEN_LETTERS,game.getListOfChosenLetters()));
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
        while (!validateAnswer(answer, regex)  &&  answer!=null) {
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
            game.broadcast(String.format(GameMessages.WON_BONUS,playerHandler.getName(),bonus, letter));
            guessQuoteFlow();
            return;
        }

        game.broadcast(String.format(GameMessages.FAIL_ANSWER,playerHandler.getName(),letter));
    }

    /**
     * Checks is a letter was already chosen by a player
     *
     * @param letter A String containing the letter
     * @return true if it was already chosen, false otherwise
     */
    private boolean checkedIfLetterIsDoubled(String letter){
        return Arrays.asList(game.getListOfChosenLetters()
                .split(", ")).contains(letter);
    }

    /**
     * Verifies if a letter given by a player was already chosen
     * @param isVowel Set it true if you want to check the vowel flow, false for consonant flow
     * @return true if letter is already present, false if it's not present
     */
    private String getAlreadyChosenRegex( boolean isVowel){
        String regex=String.join("", game.getListOfChosenLetters().split(", "));
        if(isVowel){
            regex=  "[aeiou&&[^".concat(regex).concat("]]");
        }else {
            regex= "[a-z&&[^aeiou".concat(regex).concat("]]");
        }
        return regex;
    }

    /**
     * Ask a player to pick an consonant
     * Add the consonant that player send to the letters array
     * Check if the consonant exist in quote
     */
    private void consonantFlow() {
        String consonantRegex = "[a-z&&[^aeiou]]"; //Only accept the words from a-z exclude the vowels
        String consonant;

        consonant=getPlayerAnswer(GameMessages.CHOOSE_A_CONSONANT, consonantRegex, GameMessages.INVALID_CONSONANT);
        while (checkedIfLetterIsDoubled(consonant)) {
            consonant=getPlayerAnswer(GameMessages.INVALID_DOUBLE_CONSONANT, getAlreadyChosenRegex(false), GameMessages.INVALID_DOUBLE_CONSONANT);
        }
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
        vowel=getPlayerAnswer(GameMessages.CHOOSE_A_VOWEL, vowelRegex, GameMessages.INVALID_VOWEL);
        while (checkedIfLetterIsDoubled(vowel)) {
            vowel=getPlayerAnswer(GameMessages.INVALID_DOUBLE_VOWEL, getAlreadyChosenRegex(true), GameMessages.INVALID_DOUBLE_VOWEL);
        }
        playerHandler.removeCash(VOWEL_PRICE);
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
        playerHandler.send(GameMessages.GUESS_QUOTE);
        String answer;
        answer=getMessageFromBuffer();
        if(answer==null){ //occurs when suddenly a player closes client
            return;
        }
        if (checkIfPlayerGuessedQuote(answer)) {
            Arrays.stream(game.getQuoteToGuess().split(""))
                    .filter(l -> l.matches("[a-z]"))
                    .forEach(game::addPlayerLetters);
            playerHandler.addCash(WINNER_BONUS);
            game.broadcast(game.drawBoard());
            game.broadcast(BIG_WINNER);
            game.broadcast(String.format(GameMessages.PLAYER_WON,playerHandler.getName(),playerHandler.getPlayerCash(),answer));
            game.endGame();
            return;
        }
        game.broadcast(String.format(GameMessages.FAIL_ANSWER,playerHandler.getName(),answer) );
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
