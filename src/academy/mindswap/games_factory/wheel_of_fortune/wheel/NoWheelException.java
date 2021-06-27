/*
 * @(#)NoWheelException.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.wheel;

public class NoWheelException extends Exception{
    public NoWheelException() {
        super("You need to create a wheel first");
    }
}
