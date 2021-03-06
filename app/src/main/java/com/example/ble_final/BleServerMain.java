package com.example.ble_final;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattServerCallback;
import android.os.Build;
import android.os.ParcelUuid;
import java.util.UUID;
import android.content.Context;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BleServerMain extends AdvertiseCallback {

    //UUID
    private static final String SERVICE_UUID_YOU_CAN_CHANGE = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String CHAR_UUID_YOU_CAN_CHANGE = "00002a29-0000-1000-8000-00805f9b34fb";

    //アドバタイズの設定
    private static final boolean CONNECTABLE = true;
    private static final int TIMEOUT = 0;

    //BLE
    private BluetoothLeAdvertiser advertiser;
    private BluetoothGattServer gattServer;

    //アドバタイズを開始
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startAdvertise(Context context) {

        //BLE各種を取得
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = manager.getAdapter();
        advertiser = getAdvertiser(adapter);
        gattServer = getGattServer(context, manager);

        //UUIDを設定
        setUuid();

        //アドバタイズを開始
        advertiser.startAdvertising(makeAdvertiseSetting(),makeAdvertiseData(),this);
    }

    //アドバタイズを停止
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopAdvertise() {

        //サーバーを閉じる
        if (gattServer != null) {
            gattServer.clearServices();
            gattServer.close();
            gattServer = null;
        }

        //アドバタイズを停止
        if (advertiser != null) {
            advertiser.stopAdvertising(this);
            advertiser = null;
        }
    }

    //Advertiserを取得
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private BluetoothLeAdvertiser getAdvertiser(BluetoothAdapter adapter) {
        return adapter.getBluetoothLeAdvertiser();
    }

    //GattServerを取得
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothGattServer getGattServer(Context context, BluetoothManager manager) {
        return manager.openGattServer(context, new BleServer(gattServer));
    }

    //UUIDを設定
    private void setUuid() {

        //serviceUUIDを設定
        BluetoothGattService service = new BluetoothGattService(
                UUID.fromString(SERVICE_UUID_YOU_CAN_CHANGE),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //characteristicUUIDを設定
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
                UUID.fromString(CHAR_UUID_YOU_CAN_CHANGE),
                BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ |
                        BluetoothGattCharacteristic.PERMISSION_WRITE);

        //characteristicUUIDをserviceUUIDにのせる
        service.addCharacteristic(characteristic);

        //serviceUUIDをサーバーにのせる
        gattServer.addService(service);
    }

    //アドバタイズを設定
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private AdvertiseSettings makeAdvertiseSetting() {

        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();

        //アドバタイズモード
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        //アドバタイズパワー
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW);
        //ペリフェラルへの接続を許可する
        builder.setConnectable(CONNECTABLE);
        //調査中。。
        builder.setTimeout(TIMEOUT);

        return builder.build();
    }

    //アドバタイズデータを作成
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private AdvertiseData makeAdvertiseData() {

        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.addServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_UUID_YOU_CAN_CHANGE)));

        return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
     public static class BleServer extends BluetoothGattServerCallback {

        //BLE
        private BluetoothGattServer bluetoothGattServer;
        public BleServer(BluetoothGattServer gattServer) {
            this.bluetoothGattServer = gattServer;
        }

        //セントラル（クライアント）からReadRequestが来ると呼ばれる
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onCharacteristicReadRequest(android.bluetooth.BluetoothDevice device, int requestId,
                                                int offset, BluetoothGattCharacteristic characteristic) {

            //セントラルに任意の文字を返信する
            characteristic.setValue("something you want to send");
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                    characteristic.getValue());

        }

        //セントラル（クライアント）からWriteRequestが来ると呼ばれる
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onCharacteristicWriteRequest(android.bluetooth.BluetoothDevice device, int requestId,
                                                 BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded,
                                                 int offset, byte[] value) {

            //セントラルにnullを返信する
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }
    }
}