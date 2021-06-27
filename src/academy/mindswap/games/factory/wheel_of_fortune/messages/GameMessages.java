/*
 * @(#)GameMessages.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory.wheel_of_fortune.messages;

/**
 * Messages sent to playerClient by the game
 */
public final class GameMessages {
    public static final String PERMISSION_TO_TALK = "/talk now\n";
    public static final String ASK_NAME = "Write your name!\n"+PERMISSION_TO_TALK;
    public static final String WAITING_FOR_OTHER_PLAYERS = "Waiting for other players to join the game...";
    public static final String WELCOME_MESSAGE = "--Welcome to WHEEL OF FORTUNE, be prepared to win!--\n";
    public static final String PLAYER_JOINED = "Player %s joined the game!\n";
    public static final String START_GAME = "\n--The WHEEL OF FORTUNE will start!--\n";
    public static final String THIS_IS_THE_QUOTE = "This is the quote that all of you will try to guess\n";
    public static final String SPIN_WHEEL = "The wheel is spinning, good luck!\n";
    public static final String WAITING_PLAYER_OPTION = "We're waiting for %s to choose an option...\n";
    public static final String PLAYER_CHOOSE_A_CONSONANT = "We're waiting for %s to guess a consonant...\n";
    public static final String PLAYER_CHOOSE_A_VOWEL = "We're waiting for %s to guess a vowel...\n";
    public static final String PLAYER_CHOOSE_GUESS = "We're waiting for %s to guess the quote...\n";
    public static final String CHOOSE_A_CONSONANT = "Please choose a consonant:\n"+PERMISSION_TO_TALK;
    public static final String CHOOSE_A_VOWEL = " Please choose a vowel:\n"+PERMISSION_TO_TALK;
    public static final String GUESS_QUOTE = "Please try to guess the quote:" + "\n" + PERMISSION_TO_TALK;
    public static final String INVALID_CONSONANT = " Please choose a valid consonant:\n"+PERMISSION_TO_TALK;
    public static final String INVALID_VOWEL = " Please choose a valid vowel:\n"+PERMISSION_TO_TALK;
    public static final String FAIL_ANSWER = "%s has answered %s and... Wrong answer, try again next turn\n\n";
    public static final String BANKRUPT = "It's bad luck for %s. Bankrupt means lose all money\n";
    public static final String MONEY_OPTIONS = "Choose one option!\n"+
            "a - Choose a consonant\n" +
            "b - Try to guess the quote\n" +
            "c - Buy a vowel per 3000\n" + PERMISSION_TO_TALK;
    public static final String NO_MONEY_BUY_VOWEL = "Choose one option!\n"+
            "a - Choose a consonant\n" +
            "b - Try to guess the quote\n" + PERMISSION_TO_TALK;
    public static final String INVALID_OPTION = " Please choose a valid option:\n"+PERMISSION_TO_TALK;
    public static final String MISS_TURN= "%s loss the turn, wait for the next turn :(\n";
    public static final String FREE_PLAY= "%s is very lucky he will play twice\n";
    public static final String WON_BONUS= "%s won %d by chosen the letter %s and now is trying to guess the quote...\n";
    public static final String CHOSEN_LETTERS = "These are the letters chosen until now [%s] \n";
    public static final String PLAYER_TURN = "%s is playing now\n";
    public static final String PLAYER_WON = "%s is the BIG WINNER and leaves with %d€ on his pocket\n\nThe quote was: %s\n\n";
    public static final String PLAYER_LEFT_GAME = "%s has left the game\n";
    public static final String PLAYER_CASH = "%s available cash %d";
    public static final String GAME_END = "This game is now closed. Nice to see you\n";
}
