package com.example.ble_final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.UUID;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BLE_Connect";
    BleCliant BleC = new BleCliant();
    BleServer BleS = new BleServer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        スタートボタンの探索
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                BleC.connect();

            }
        });

    }
}
