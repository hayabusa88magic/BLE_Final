package com.example.ble_final;


import java.util.Timer;
import java.util.TimerTask;


public class UtilTimerTest {

        public UtilTimerTest() {
        timer = new Timer(true);
        time = new Time();
        }//utilltimertest

        public void start() {
            if (task == null) {
                    task = new Task(time);
            }
            System.out.println("Taskを開始します");
            timer.schedule(task, 0, 1000);
        }//start


        public void stop() {
            task.cancel();
            task = null;
            System.out.println("Taskが停止しました");
        }//stop

            /** 一定時間毎にタスクを起動するTimer */
            private Timer timer = null;
            /** Timerから一定時間毎に起動されるタスク */
            private Task task = null;
            /**  時刻を保持するオブジェクト */
            private Time time = null;

} // UtilTimerTest