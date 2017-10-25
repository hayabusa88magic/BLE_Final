package com.example.ble_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.os.Handler;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.Sensor;
import android.app.Activity;
import android.bluetooth.*;

public class MainActivity extends Activity {

    private static final String TAG = "MAIN";
    BleCliant BleC = new BleCliant();
    BleServerMain.BleServer BleS = new BleServerMain.BleServer();
    UtilTimerTest utilTimer = new UtilTimerTest();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }//oncreate
}