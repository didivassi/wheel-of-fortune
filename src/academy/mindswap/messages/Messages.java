package academy.mindswap.messages;

import academy.mindswap.game.Game;

public class Messages {
    public static final String PERMISSION_TO_TALK = "/talk now\n";
    public static final String ASK_NAME = "Write your name!\n"+PERMISSION_TO_TALK;
    public static final String WELCOME_MESSAGE = "Welcome to game, be prepared to win!\n";
    public static final String START_GAME = "The game will start!\n";
    public static final String SPIN_WHEEL = "The wheel is spinning, we wish you luck!\n";
    public static final String CHOOSE_A_CONSONANT = " Please choose a consonant: \n"+PERMISSION_TO_TALK;
    public static final String CHOOSE_A_VOWEL = " Please choose a vowel: \n"+PERMISSION_TO_TALK;
    public static final String INVALID_VOWEL = " Please choose a valid vowel: \n"+PERMISSION_TO_TALK;
    public static final String INVALID_CONSONANT = " Please choose a valid consonant: \n"+PERMISSION_TO_TALK;
    public static final String INVALID_OPTION = " Please choose a valid option: \n"+PERMISSION_TO_TALK;
    public static final String BANKRUPT = "It's bad the player %s loss all money \n";
    public static final String MONEY_OPTIONS = "Choose one option!\n"+
            "a - Choose a consonant\n" +
            "b - Buy a vowel per 3000\n" +
            "c - Try to guess the quote\n" + PERMISSION_TO_TALK;
    public static final String MISS_TURN= "The player %s loss the turn, wait for the next turn :( \n";
    public static final String WON_BONUS= "%s won %d by chosen the letter %s " + "\n";
    public static final String GUESS_QUOTE = "Please try to guess the quote " + "\n" + PERMISSION_TO_TALK;
    public static final String FAIL_ANSWER = "Wrong answer, try again next turn \n";
    public static final String CHOSEN_LETTERS = "This are the letters chosen until now \n";
}
