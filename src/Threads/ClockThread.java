package Threads;

import GUI.GUI;

import java.util.Date;

public class ClockThread extends Thread {

    GUI gui;
    String time;

    public ClockThread(GUI gui){
        this.gui = gui;
        start();
    }

    public void run(){
        while(true){
            time = "" + new Date();
            gui.jLabelClock.setText(time);
        }
    }
}
