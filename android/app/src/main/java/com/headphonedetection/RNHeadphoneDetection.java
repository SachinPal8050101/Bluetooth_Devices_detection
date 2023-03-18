package com.headphonedetection;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNHeadphoneDetection extends ReactContextBaseJavaModule {
    private BroadcastReceiver myReceiver;

    public RNHeadphoneDetection(ReactApplicationContext reactContext) {
        super(reactContext);

        // Create the BroadcastReceiver
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                System.out.println("ddd"+ action);

                switch (action) {
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        Log.d("RNHeadphoneDetection", "Blutooth Connected");
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("AUDIO_DEVICE_CHANGED_NOTIFICATION", null);
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        Log.d("RNHeadphoneDetection", "Bluthooth Disconnected: ");
                        getReactApplicationContext()
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("AUDIO_DEVICE_CHANGED_NOTIFICATION", null);
                        break;
                    case Intent.ACTION_HEADSET_PLUG:
                        Log.d("RNHeadphoneDetection", "Pluged EarPhone conneted Disconnected: ");
                        int state = intent.getIntExtra("state", -1);
                        if (state == 0) {
                            Log.d("HeadsetPlugReceiver", "Headset unplugged!");
                        } else if (state == 1) {
                            Log.d("HeadsetPlugReceiver", "Headset plugged in!");
                        }
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
        return "RNHeadphoneDetection";
    }

    @ReactMethod
    public void myNativeMethod(String message) {
        Log.d("RNHeadphoneDetection", "Received message: " + message);
    }

    @ReactMethod
    public void sendBroadcast() {
        // Send a broadcast with the action "my_action"
        Intent intent = new Intent(Intent.ACTION_HEADSET_PLUG);
        getReactApplicationContext().sendBroadcast(intent);
    }

    public WritableMap isAudioDeviceConnected() {
        AudioManager audioManager = (AudioManager) getReactApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo wiredHeadphone = null;
        AudioDeviceInfo bluetoothHeadset = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                int deviceType = deviceInfo.getType();
                if (deviceType == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || deviceType == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    wiredHeadphone = deviceInfo;
                } else if (deviceType == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || deviceType == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    bluetoothHeadset = deviceInfo;
                }
            }
        }

        WritableMap audioDeviceInfo = Arguments.createMap();
        if (wiredHeadphone != null) {
            audioDeviceInfo.putBoolean("wiredHeadphone",true);
            audioDeviceInfo.putBoolean("bluetoothHeadset",false);
        }
        else if (bluetoothHeadset != null) {
            audioDeviceInfo.putBoolean("wiredHeadphone",false);
            audioDeviceInfo.putBoolean("bluetoothHeadset",true);
        }else{
            audioDeviceInfo.putBoolean("wiredHeadphone",false);
            audioDeviceInfo.putBoolean("bluetoothHeadset",false);
        }
        return audioDeviceInfo;
    }

    @ReactMethod
    public void isAudioDeviceConnected(final Promise promise) {
        promise.resolve(isAudioDeviceConnected());
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();

        // Unregister the BroadcastReceiver when the Catalyst instance is destroyed
        getReactApplicationContext().unregisterReceiver(myReceiver);
    }
}

