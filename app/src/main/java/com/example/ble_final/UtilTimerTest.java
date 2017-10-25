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

        /**
         * Timerを開始(再開)する。
         * Timerに登録するTaskがnullであれば、新規にTaskを生成してから、これを
         * Timerにセットしている。再開時に前回の状態を引き継ぐため、Taskの生成
         * 時にtimeオブジェクトを渡している。
         */
        public void start() {
            if (task == null) {
                task = new TestTask(time);
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

        /**
         * Timerのテストプログラムのmainメソッド。
         * 10秒毎に、テストの開始、終了を交互に呼び出す。
         *
         * @throws InterruptedException タイミングを取るためにThreadクラスの
         * ブロッキング・メソッドsleepを呼び出しているが、意図しないインタラプトが
         * 発生したときにスローする
         */
        public static void main(String[] args) throws InterruptedException {
            UtilTimerTest tester = new UtilTimerTest();

            tester.start();
            Thread.sleep(10 * 1000);
            tester.stop();
            Thread.sleep(10 * 1000);
            tester.start();
            Thread.sleep(10 * 1000);
            tester.stop();
            Thread.sleep(10 * 1000);
            tester.start();
            Thread.sleep(10 * 1000);
            tester.stop();
        }

        /** 一定時間毎にタスクを起動するTimer */
        private Timer timer = null;
        /** Timerから一定時間毎に起動されるタスク */
        private TestTask task = null;
        /**  時刻を保持するオブジェクト */
        private Time time = null;

    } // UtilTimerTest
}

class TestTask extends TimerTask {
    private Time time;

    public TestTask(Time aTime) {
        time = aTime;
    }

    public void run() {
        time.tick();
        System.out.println("Second = " + time.getSecond());
    }
}


public class Time {
    private int second = 0;

    public void tick() {
        second++;
    }

    public int getSecond() {
        return second;
    }
}
}
