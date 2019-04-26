package Threads;

import GUI.RusGUI;

import java.util.Date;

public class ClockThreadRus extends Thread {

    RusGUI rusGUI;
    String time;

    public ClockThreadRus(RusGUI rusGUI){
        this.rusGUI = rusGUI;
        start();
    }

    public void run(){
        while(true){
            time = "" + new Date();
            rusGUI.jLabelClock.setText(time);

        }
    }
}
