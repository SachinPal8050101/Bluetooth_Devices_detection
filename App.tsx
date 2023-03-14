import {View, Text, DeviceEventEmitter, NativeModules} from 'react-native';
import React, {useEffect} from 'react';

export default function App() {
  // const MyModule = NativeModules.MyNativeModule;
  // const eventEmitter = new NativeEventEmitter(MyModule);
  useEffect(() => {
    DeviceEventEmitter.addListener(
      'IS_BLUETOOTH_HEADPHONE_STATE_CHANGED',
      () => {
        console.log('======='); // Outputs "Hello from Java!"
      },
    );
    NativeModules.MyNativeModule.myNativeMethod('Hello, world!=====');
  }, []);

  return (
    <View>
      <Text>App</Text>
    </View>
  );
}
