/*
 * @(#)Board.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.game.ascii_art;

import java.util.*;

/**
 * The static class responsible for drawing the board for the wheel of fortune game
 */
public final class Board {


    public  static final String TITLE = " __        ___               _          __   _____          _                    \n" +
                " \\ \\      / / |__   ___  ___| |   ___  / _| |  ___|__  _ __| |_ _   _ _ __   ___ \n" +
                "  \\ \\ /\\ / /| '_ \\ / _ \\/ _ \\ |  / _ \\| |_  | |_ / _ \\| '__| __| | | | '_ \\ / _ \\\n" +
                "   \\ V  V / | | | |  __/  __/ | | (_) |  _| |  _| (_) | |  | |_| |_| | | | |  __/\n" +
                "    \\_/\\_/  |_| |_|\\___|\\___|_|  \\___/|_|   |_|  \\___/|_|   \\__|\\__,_|_| |_|\\___|\n";

    public static final String SUB_HEADING = "   _,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,__,.-'~'-.,_\n";
    public static final String BOARD_TOP = "       __   __________________________________________________________   __\n";
    public static final String BOARD_EMPTY_SPACE =  "         | |                                                          | |\n";
    public static final String BOARD_MIDDLE_SEPARATOR = "       __| |__________________________________________________________| |__\n"+
                                                    "       __   __________________________________________________________   __\n";
    public static final String BOARD_PLAYERS_SEPARATOR = "       __| |__________________________________________________________| |__\n";
    public static final String BOARD_FOOTER = "       __| |__________________||__________________||__________________| |__\n\n";
    public static final String QUOTE_WHITE_SPACES = "                                                          ";
    public static final String PLAYER_WHITE_SPACES = "                  ";

    /**
     * Draws a leader board for the Wheel of Fortune game in ASCII art
     *
     * @param quote a String that represents the quote to display
     * @param letters a Set of individual strings that were already picked by the players
     * @param players A List<List<String>> of 3 players and their score.
     *                Example n  json [{player1Name,player1Score},{player2Name,player2Score},{player3Name,player3Score}]
     * @return A String with the drawn board
     */
    public static String  drawBoard(String quote, Set<String> letters, List<List<String>> players){

        List<String>   player1 = players.get(0);
        List<String> player2 = players.get(1);
        List<String> player3 = players.get(2);
        String[] splicedQuote = splitStringRows(quote);
        String lettersTaken= "Chosen:".concat(String.join(",",letters));
        return          TITLE +
                SUB_HEADING +
                BOARD_TOP +
                BOARD_EMPTY_SPACE +
                        "         | |"+centerInWhiteSpaces(QUOTE_WHITE_SPACES,splicedQuote[0])+"| |\n"+
                        "         | |"+centerInWhiteSpaces(QUOTE_WHITE_SPACES,splicedQuote[1])+"| |\n"+
                BOARD_EMPTY_SPACE +
                BOARD_MIDDLE_SEPARATOR +
                        "         | |"+alignLeftInWhiteSpaces(QUOTE_WHITE_SPACES,lettersTaken)+"| |\n"+
                BOARD_PLAYERS_SEPARATOR +
                "         | |"+centerInWhiteSpaces(PLAYER_WHITE_SPACES, player1.get(0)) +
                          "||"+centerInWhiteSpaces(PLAYER_WHITE_SPACES, player2.get(0)) +
                          "||"+centerInWhiteSpaces(PLAYER_WHITE_SPACES, player3.get(0)) + "| |  \n"+
                "         | |"+centerInWhiteSpaces(PLAYER_WHITE_SPACES, player1.get(1)) +"||"+
                            centerInWhiteSpaces(PLAYER_WHITE_SPACES, player2.get(1)) + "||"+
                            centerInWhiteSpaces(PLAYER_WHITE_SPACES,player3.get(1)) +"| |  \n"+
                BOARD_FOOTER;


    }

    /**
     * Splits a quote into two half's
     * @param quote the String to be split
     *
     * @return A String[] containing the two half's of the quote
     */
    private static String[] splitStringRows(String quote){
        String[] wordsArray=quote.split(" ");
        int len = wordsArray.length;
        String firstHalf = String.join(" ", Arrays.copyOfRange(wordsArray, 0, len / 2));
        String secondHalf = String.join(" ", Arrays.copyOfRange(wordsArray, (len / 2), len));

        return new String[]{firstHalf, secondHalf};

    }

    /**
     * Centers a phrase in the middle of an given string of white spaces
     *
     * @param whiteSpaces A String containing only white spaces
     * @param toCenter A String containing the phrase to center
     * @return A String containing the phrase centered in the white spaces
     */
    private static String centerInWhiteSpaces(String whiteSpaces,String toCenter ){
        int startingIndex;
        final int i = (whiteSpaces.length() - toCenter.length()) / 2;
        if(toCenter.length()%2==0){
            startingIndex= i;
        }else {
            startingIndex= i +1;
        }
        return whiteSpaces
                .substring(0,startingIndex)
                .concat(toCenter)
                .concat(whiteSpaces.substring(startingIndex+toCenter.length()));
    }

    /**
     * Aligns left a phrase in the of a string of white spaces
     *
     * @param whiteSpaces A String containing only white spaces
     * @param toLeft A String containing the phrase to align left
     * @return A String containing the phrase left aligned in the white spaces
     */
    private static String alignLeftInWhiteSpaces(String whiteSpaces,String toLeft ){
        return toLeft.concat(whiteSpaces
                .substring(toLeft.length()));

    }
}
