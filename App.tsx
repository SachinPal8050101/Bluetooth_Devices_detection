import {View, Text} from 'react-native';
import React, {useEffect} from 'react';

import {NativeModules} from 'react-native';

export default function App() {
  useEffect(() => {
    NativeModules.MyNativeModule.myNativeMethod('Hello, world!=====');
  }, []);

  return (
    <View>
      <Text>App</Text>
    </View>
  );
}
