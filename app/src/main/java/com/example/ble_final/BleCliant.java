package com.example.ble_final;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

public class BleCliant extends Activity implements BluetoothAdapter.LeScanCallback {

    private static final long SCAN_PERIOD = 10000;

    private static final String DEVICE_NAME = "Sensor";
    /** 対象のサービスUUID */
    private static final String DEVICE_BUTTON_SENSOR_SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    /** 対象のキャラクタリスティックUUID */
    private static final String DEVICE_BUTTON_SENSOR_CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    /** キャラクタリスティック設定UUID */
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    private static final String TAG = "BLE_Test";

    private BleStatus mStatus = BleStatus.DISCONNECTED;


    public Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private TextView mStatusText;


    private void set() {
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mStatusText = (TextView) findViewById(R.id.text_status);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mStatusText.setText(((BleStatus) msg.obj).name());
            }
        };
    }

    /** BLE機器を検索する */
    private void connect() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(BleCliant.this);
                if (BleStatus.SCANNING.equals(mStatus)) {
                    setStatus(BleStatus.SCAN_FAILED);
                }
            }
        }, SCAN_PERIOD);

        mBluetoothAdapter.stopLeScan(this);
        mBluetoothAdapter.startLeScan(this);
        setStatus(BleStatus.SCANNING);
    }

    /** BLE 機器との接続を解除する */
    private void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            setStatus(BleStatus.CLOSED);
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord ) {
        Log.d(TAG, "device found: " + device.getName());
        if (DEVICE_NAME.equals(device.getName())) {
            setStatus(BleStatus.DEVICE_FOUND);

// 省電力のためスキャンを停止する
            mBluetoothAdapter.stopLeScan(this);

// GATT接続を試みる
            mBluetoothGatt = device.connectGatt(this, false, mBluetoothGattCallback);
        }
    }

    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG,"onConnectionStateChange:" + status + " -> " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
// GATTへ接続成功
// サービスを検索する
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
// GATT通信から切断された
                setStatus(BleStatus.DISCONNECTED);
                mBluetoothGatt = null;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered received: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(UUID.fromString(DEVICE_BUTTON_SENSOR_SERVICE_UUID));
                if (service == null) {
// サービスが見つからなかった
                    setStatus(BleStatus.SERVICE_NOT_FOUND);
                } else {
// サービスを見つけた
                    setStatus(BleStatus.SERVICE_FOUND);

                    BluetoothGattCharacteristic characteristic =
                            service.getCharacteristic(UUID.fromString(DEVICE_BUTTON_SENSOR_CHARACTERISTIC_UUID));

                    if (characteristic == null) {
// キャラクタリスティックが見つからなかった
                        setStatus(BleStatus.CHARACTERISTIC_NOT_FOUND);
                    } else {
// キャラクタリスティックを見つけた

// Notification を要求する
                        boolean registered = gatt.setCharacteristicNotification(characteristic, true);

// Characteristic の Notification 有効化
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                                UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        gatt.writeDescriptor(descriptor);

                        if (registered) {
// Characteristics通知設定完了
                            setStatus(BleStatus.NOTIFICATION_REGISTERED);
                        } else {
                            setStatus(BleStatus.NOTIFICATION_REGISTER_FAILED);
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d(TAG, "onCharacteristicRead: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
// READ成功
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged");
// Characteristicの値更新通知

            if (DEVICE_BUTTON_SENSOR_CHARACTERISTIC_UUID.equals(characteristic.getUuid().toString())) {
                Byte value = characteristic.getValue()[0];

            }
        }
    };



    private void setStatus(BleStatus status) {
        mStatus = status;
        mHandler.sendMessage(status.message());
//        メソッド呼び出し
    }

    private enum BleStatus {
        //        https://qiita.com/hkusu/items/0996735553580bfabbdb →enumの使い方
        DISCONNECTED,
        SCANNING,
        SCAN_FAILED,
        DEVICE_FOUND,
        SERVICE_NOT_FOUND,
        SERVICE_FOUND,
        CHARACTERISTIC_NOT_FOUND,
        NOTIFICATION_REGISTERED,
        NOTIFICATION_REGISTER_FAILED, CLOSED ;

        public Message message() {
//           メソッドの実装
//           [修飾子] 戻り値のデータ型 メソッド名(引数1, 引数2, ....){}
            Message message = new Message();
//           オブジェクト化
//           クラス名 変数名 = new クラス名(引数);　https://www.javadrive.jp/start/about/index2.html

            message.obj = this;
//            メッセージの中のobj
            return message;
        }
    }

}
