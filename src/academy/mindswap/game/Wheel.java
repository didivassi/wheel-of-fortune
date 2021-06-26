package academy.mindswap.game;

import academy.mindswap.game.commands.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Wheel {

    private final List<Command> wheel = new LinkedList<>();

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
                option = getNotPenalty();
            }

            if (option != lastOption) {
                wheel.add(option);
                i++;
            }

            lastOption = option;
        }

    }

    private Command getPenalty() {
        Command option = getRandomWheelOption();

        if (!option.isPenalty()) {
            return getPenalty();
        }

        return option;
    }

    private int getNumberOfPenalties() {
        return (int) Arrays.stream(Command.values())
                .filter(Command::isPenalty)
                .count();
    }

    private Command getNotPenalty() {
        Command option = getRandomWheelOption();

        if (option.isPenalty()) {
            return getNotPenalty();
        }

        return option;
    }

    private Command getRandomWheelOption() {
        int position = getRandomNumber(Command.values().length);
        return Command.values()[position];
    }

    private int getRandomNumber(int max) {
        return (int) (Math.random() * (max));
    }

    public Command spinWheel(){
        return wheel.get(getRandomNumber(wheel.size() - 1));
    }

    public void animate(Command luckyOption, int turns, Game game){
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

        game.broadcast("\r" + "| " + luckyOption.toString() +" |\n");
        game.broadcast("\n");
    }

}
