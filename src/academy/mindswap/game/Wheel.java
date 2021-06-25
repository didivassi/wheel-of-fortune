package academy.mindswap.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Wheel {

    private List<WheelOptions> wheel = new LinkedList<>();

    private void createWheel(int numberOfOptions, double percentageOfPenalties) {
        int max_penalties = (int) Math.ceil(numberOfOptions * percentageOfPenalties);
        int max_penaltyFrequency = (int) Math.floor(numberOfOptions * percentageOfPenalties / getNumberOfPenalties());
        List<WheelOptions> penaltiesInWheel = new LinkedList<>();
        WheelOptions lastOption = null;
        WheelOptions option;
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


    private WheelOptions getPenalty() {
        WheelOptions option = getRandomWheelOption();
        if (!option.isPenalty()) {
            return getPenalty();
        }
        return option;
    }

    private int getNumberOfPenalties() {
        return (int) Arrays.stream(WheelOptions.values())
                .filter(WheelOptions::isPenalty)
                .count();

    }

    private WheelOptions getNotPenalty() {
        WheelOptions option = getRandomWheelOption();
        if (option.isPenalty()) {
            return getNotPenalty();
        }
        return option;
    }

    private WheelOptions getRandomWheelOption() {
        int position = getRandomNumber(WheelOptions.values().length);
        return WheelOptions.values()[position];
    }

    private int getRandomNumber(int max) {
        return (int) (Math.random() * (max));
    }

    public List<WheelOptions> getWheel() {
        return wheel;
    }

    public WheelOptions getLuckyOption(){
        return wheel.get(getRandomNumber(wheel.size() - 1));
    }

    public void animate(WheelOptions luckyOption, int turns){
        String anim=wheel.stream().map(WheelOptions::toString).collect(Collectors.joining(" | "));
        int sleep=10;
        for (int i = 0; i <= turns; i++) {
            for (int j = 0; j < anim.length()-9 ; j++) {
                System.out.print("\r"+anim.substring(j,j+9));
                try {
                    Thread.sleep(sleep);
                }catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
            sleep+=20;
        }
        System.out.print("\r"+"| "+luckyOption.toString()+" |");
    }

    public static void main(String[] args) {
        Wheel wheel = new Wheel();
        wheel.createWheel(30,0.25);
        WheelOptions luckyOption=  wheel.getLuckyOption();
        System.out.println(wheel.getWheel());
        System.out.println(luckyOption);
        wheel.animate(luckyOption,3);
    }

}
