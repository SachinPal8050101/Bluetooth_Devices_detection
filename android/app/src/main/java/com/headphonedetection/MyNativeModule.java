package com.headphonedetection;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MyNativeModule extends ReactContextBaseJavaModule {
    private BroadcastReceiver myReceiver;

    public MyNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);

        // Create the BroadcastReceiver
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                switch (action) {
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        Log.d("MyNativeModule", "Blutooth Connected");
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("IS_BLUETOOTH_HEADPHONE_STATE_CHANGED", null);
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        Log.d("MyNativeModule", "Bluthooth Disconnected: ");
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("IS_BLUETOOTH_HEADPHONE_STATE_CHANGED", null);
                        break;
                    case Intent.ACTION_HEADSET_PLUG:
                        Log.d("MyNativeModule", "Pluged EarPhone conneted Disconnected: ");
                        int state = intent.getIntExtra("state", -1);
                        if (state == 0) {
                            Log.d("HeadsetPlugReceiver", "Headset unplugged!");
                        } else if (state == 1) {
                            Log.d("HeadsetPlugReceiver", "Headset plugged in!");
                        }
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("IS_BLUETOOTH_HEADPHONE_STATE_CHANGED", null);
                        break;
                    default:
                        break;
                }


                // Handle broadcast received
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        reactContext.registerReceiver(myReceiver, filter);
    }

    @Override
    public String getName() {
        return "MyNativeModule";
    }

    @ReactMethod
    public void myNativeMethod(String message) {
        Log.d("MyNativeModule", "Received message: " + message);
    }

    @ReactMethod
    public void sendBroadcast() {
        // Send a broadcast with the action "my_action"
        Intent intent = new Intent(Intent.ACTION_HEADSET_PLUG);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();

        // Unregister the BroadcastReceiver when the Catalyst instance is destroyed
        getReactApplicationContext().unregisterReceiver(myReceiver);
    }
}

