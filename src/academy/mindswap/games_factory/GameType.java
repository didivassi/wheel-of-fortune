/*
 * @(#)GameType.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory;

/**
 * Enum with the game types available on the server
 */
public enum GameType {
    WHEEL_OF_FORTUNE("Wheel of Fortune"),
    BINGO("Bingo"),
    QUIZ("Quiz");

    private final String description;

    /**
     *
     * @param description is the description name of the game
     */
    GameType(String description) {
        this.description = description;
    }
}
