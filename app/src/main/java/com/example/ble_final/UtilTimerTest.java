package com.example.ble_final;

/**
 * Created by Shunya_F on 2017/10/25.
 */

import java.util.Timer;
import java.util.TimerTask;

    /**
     * UtilTimerTest.java
     * java.util.Timerの使い方を検証するテストコード。
     * 一時停止、再開を行いたい。
     *
     * @author <a href="mailto:torutk@alles.or.jp">Toru TAKAHASHI</a>
     * @version $Id: UtilTimerTest.java 72 2007-05-09 23:19:38Z toru $
     */

    public class UtilTimerTest {


    public UtilTimerTest() {
        timer = new Timer(true);
        time = new Time();
    }

    public void start() {
        if (task == null) {
                task = new Task(time);
        }
        System.out.println("Taskを開始します");
        timer.schedule(task, 0, 1000);
    }

        /**
         * Timerを停止する。
         * 実は、Timerに登録したTaskオブジェクトをcancelしてから破棄(=null)して
         * いる。
         */
    public void stop() {
        task.cancel();
        task = null;
        System.out.println("Taskが停止しました");
    }

        /** 一定時間毎にタスクを起動するTimer */
        private Timer timer = null;
        /** Timerから一定時間毎に起動されるタスク */
        private Task task = null;
        /**  時刻を保持するオブジェクト */
        private Time time = null;

    } // UtilTimerTest
}