/*
 * @(#)GamesFactory.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games.factory;

import academy.mindswap.games.factory.bingo.Bingo;
import academy.mindswap.games.factory.quiz.Quiz;
import academy.mindswap.games.factory.wheel_of_fortune.WheelOfFortune;
import academy.mindswap.server.Server;

/**
 * Is the factory of games, when the type of games available are created
 */
public class GamesFactory {

    /**
     *
     * @param server is a instanced of server class
     * @param type is the type of game that is chosen by player
     * @return a new game type
     */
    public static Game create(Server server, GameType type){
        switch(type){
            case WHEEL_OF_FORTUNE:
                return new WheelOfFortune(server);
            case BINGO:
                return new Bingo(server);
            default:
                return new Quiz(server);
        }
    }
}
