/*
 * @(#)Wheel.java        1.0 28/06/2021
 *
 * Copyright (c) MindSwap Academy - Manuela Dourado, Filipa Bastos & Diogo Velho
 * All rights reserved.
 *
 * This software was produced to become our first group project.
 */
package academy.mindswap.game.wheel;

import academy.mindswap.game.Game;
import academy.mindswap.game.commands.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for creating a wheel of fortune with different random outcomes provided by the enum Command class
 * It's also responsible for sending the wheel spinning animation for the players. Needs access to Game's broadcast method
 * Invoking the spinWheel method it will return an random enum from the Command class
 */
public class Wheel {

    private final List<Command> wheel = new LinkedList<>();

    /**
     * Creates a distributed random wheel of fortune with different outcomes provided by the enum Command class
     * Before the wheel is spun this method must be invoked;
     * @param numberOfOptions the number of different outcomes that the wheel will have
     * @param percentageOfPenalties the percentage between 0 and 1 of penalties that the wheel has. 0.25 is recommended
     */
    public void createWheel(int numberOfOptions, double percentageOfPenalties) {
        int max_penalties = (int) Math.ceil(numberOfOptions * percentageOfPenalties);
        int max_penaltyFrequency = (int) Math.floor(numberOfOptions * percentageOfPenalties / getNumberOfPenalties());
        List<Command> penaltiesInWheel = new LinkedList<>();
        Command lastOption = null;
        Command option;
        int i = 0;

        while (wheel.size() <= numberOfOptions) {

            if (i % max_penalties == 0 && penaltiesInWheel.size() <= max_penalties) {
                option = getPenalty();

                while (Collections.frequency(penaltiesInWheel, option) >= max_penaltyFrequency) {
                    option = getPenalty();
                }

                penaltiesInWheel.add(option);

            } else {
                option = getBonus();
            }

            if (option != lastOption) {
                wheel.add(option);
                i++;
            }

            lastOption = option;
        }

    }

    /**
     * Method to get a random penalty Command
     * @return a Command that is not a MoneyHandler
     */
    private Command getPenalty() {
        Command option = getRandomWheelOption();

        if (!option.isPenalty()) {
            return getPenalty();
        }

        return option;
    }

    /**
     * Method to get a random Command money Bonus
     * @return a Command MoneyHandler
     */
    private Command getBonus() {
        Command option = getRandomWheelOption();

        if (option.isPenalty()) {
            return getBonus();
        }

        return option;
    }

    /**
     * Method to get the number of penalties already present at the wheel
     * @return an int with the number of penalties
     */
    private int getNumberOfPenalties() {
        return (int) Arrays.stream(Command.values())
                .filter(Command::isPenalty)
                .count();
    }


    /**
     * Method to return a random Command from enum Command class
     * @return a random Command
     */
    private Command getRandomWheelOption() {
        int position = getRandomNumber(Command.values().length);
        return Command.values()[position];
    }

    /**
     * Method to generate a random int from 0 until max
     * @param max the max random number to generate
     * @return a random int between 0 and max
     */
    private int getRandomNumber(int max) {
        return (int) (Math.random() * (max));
    }

    /**
     * Method to get a random Command from the created wheel
     * @return a random Command from enum Command class
     * @throws NoWheelException when no wheel was created before invoking this method
     */
    public Command spinWheel() throws NoWheelException{
        if(wheel.size()==0){
            throw new NoWheelException();
        }

        return wheel.get(getRandomNumber(wheel.size() - 1));
    }

    /**
     * Method responsible for broadcasting to players clients the wheel animation.
     * It goes trough the created wheel and sends a substring of 9 chars followed by a \r
     * to clear the line on PlayerClient's console.
     * For the animation to work the PlayerClient's must be reading the sent message char by char.
     * @param luckyOption the Command from the enum Command that will be shown at the end of the class
     * @param turns the amount of turns that wheel will spin before stop
     * @param game the needed game object with a broadcast method to use to send the spinning animation to the players
     * @throws NoWheelException when no wheel was created before invoking this method
     * @throws NullGameException when the provided game object is null
     */
    public void animate(Command luckyOption, int turns, Game game) throws NoWheelException, NullGameException  {
        if(wheel.size()==0){
            throw new NoWheelException();
        }
        if (game==null){
            throw new NullGameException();
        }

        String anim=wheel.stream().map(Command::toString).collect(Collectors.joining(" | "));
        int sleep=10;
        game.broadcast("\n");

        for (int i = 0; i <= turns; i++) {

            for (int j = 0; j < anim.length()-9 ; j++) {
                game.broadcast("\r" + anim.substring(j,j+9));

                try {
                    Thread.sleep(sleep);
                }catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }

            sleep+=20;
        }

        game.broadcast("\r" + "| " + luckyOption.toString() +" | <- The wheel says\n");
        game.broadcast("\n");
    }

}
