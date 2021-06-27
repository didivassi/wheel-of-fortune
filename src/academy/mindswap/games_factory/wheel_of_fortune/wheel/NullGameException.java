/*
 * @(#)NullGameException.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */

package academy.mindswap.games_factory.wheel_of_fortune.wheel;

public class NullGameException extends Exception{
    public NullGameException() {
        super("The provided game object is null");
    }
}
