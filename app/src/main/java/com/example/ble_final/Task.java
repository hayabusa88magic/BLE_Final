package com.example.ble_final;

import java.util.TimerTask;

public  class Task extends TimerTask {
    private Time time;

    public Task(Time aTime) {

        time = aTime;
    }

    public void run() {
        time.tick();
        System.out.println("Second = " + time.getSecond());
    }
}
