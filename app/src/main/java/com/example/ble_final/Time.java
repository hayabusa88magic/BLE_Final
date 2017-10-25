package com.example.ble_final;

public class Time {
    private int second = 0;

    public void tick() {
        second++;
    }

    public int getSecond() {
        return second;
    }
}
