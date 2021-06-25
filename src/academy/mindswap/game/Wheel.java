package academy.mindswap.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Wheel {

    private List<WheelOptions> wheel = new LinkedList<>();
    private void createWheel(int numberOfOptions){
        int max_penalties = (int) Math.ceil(numberOfOptions/4);
        int max_penaltyFrequency=(int) Math.ceil(numberOfOptions/4/3);
        List<WheelOptions> penaltiesInWheel = new LinkedList<>();
        WheelOptions lastOption=null;
        WheelOptions option=null;

        int i = 0;
            while (wheel.size()<=numberOfOptions){

                if(i % max_penalties==0 &&  penaltiesInWheel.size()<=max_penalties){
                    option=getPenalty();
                    while (Collections.frequency(penaltiesInWheel,option)>max_penaltyFrequency){
                        option=getPenalty();
                    }
                    penaltiesInWheel.add(option);
                }else {
                    option=getNotPenalty();
                }

                if(option!=lastOption){
                    wheel.add(option);
                    i++;
                }

                lastOption=option;
            }

    }



    private WheelOptions getPenalty(){
        WheelOptions option;
        while (!(option=getRandomWheelOption()).isPenalty());
        return option;
    }

    private WheelOptions getNotPenalty(){
        WheelOptions option;
        while ((option=getRandomWheelOption()).isPenalty());
        return option;
    }
    private WheelOptions getRandomWheelOption(){
        int position = getRandomNumber(WheelOptions.values().length);
        return WheelOptions.values()[position];
    }

    public int getRandomNumber( int max) {
        return (int) (Math.random() * (max));
    }

    public List<WheelOptions> getWheel(){
        return wheel;
    }

    public static void main(String[] args) {
            Wheel wheel  =new Wheel();
            wheel.createWheel(20);
            System.out.println(wheel.getWheel());
    }

}
