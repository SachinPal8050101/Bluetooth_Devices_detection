import {View, Text, DeviceEventEmitter, NativeModules} from 'react-native';
import React, {useEffect} from 'react';

export default function App() {
  // const eventEmitter = new NativeEventEmitter(MyModule);
  useEffect(() => {
    // console.log(
    //   MyModule.isAudioDeviceConnected().then((res: any) => {
    //     console.log(res);
    //   }),
    // );
    DeviceEventEmitter.addListener('AUDIO_DEVICE_CHANGED_NOTIFICATION', () => {
      console.log('======='); // Outputs "Hello from Java!"
      setTimeout(() => {
        NativeModules.RNHeadphoneDetection.isAudioDeviceConnected().then(
          (res: any) => {
            console.log('===>>>>>', res);
          },
        );
      }, 4000);
    });
  }, []);

  return (
    <View>
      <Text>App</Text>
    </View>
  );
}
