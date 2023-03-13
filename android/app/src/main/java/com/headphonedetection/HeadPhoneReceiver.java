package com.headphonedetection;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadPhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            // Bluetooth device is connected
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//            Log.d("BluetoothReceiver", "Device connected: " + device.getName());
           System.out.println("===>>Connected");
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            // Bluetooth device is disconnected
            System.out.println("===>>Disonnected");

        } else if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            // Earphone is connected or disconnected
            System.out.println("===>>HeadphoneConnectedAndDisconnected");
        }

    }
}
