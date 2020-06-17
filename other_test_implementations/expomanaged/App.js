import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, Vibration, Button } from 'react-native';

import { Audio } from 'expo-av';
import * as FileSystem from 'expo-file-system';
import * as SecureStore from 'expo-secure-store';
import { Asset } from 'expo-asset';

export default class Application extends Component {
  constructor () {
    super();
    this.state = {
      content: ''
    };
  }

  render() {
    return (
      <View style={styles.container}>
         <Button
            title="Run Tests"
            onPress={() => { runTests((test) => { this.setState({ content: test }); }); }}
          />
        <Text>{ this.state.content }</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

Math.ackermann = function (m, n) {
    return m === 0 ? n + 1 : Math.ackermann(m - 1, n === 0 ? 1 : Math.ackermann(m, n - 1));
}

class Test {
  constructor (name, callback, test) {
    console.log('CREATED TEST ' + name);
    this.name = name;
    this.callback = callback;
    this.test = test;

    this.values = [];
  }

  execute () {
      console.log(this.values.length);
    if (this.values.length < (this.name === 'AudioTest' ? 10 : 100)) {
      this.test((value) => {
        this.values.push(value || 1);
        this.execute();
      });
    } else {
      this.values.sort((a, b) => a - b);
      this.callback({
        min: Math.min(... this.values).toFixed(3),
        max: Math.max(... this.values).toFixed(3),
        avg: (this.values.reduce((a, b) => a + b, 0) / (this.name === 'AudioTest' ? 10 : 100)).toFixed(3),
        med: this.values[(this.name === 'AudioTest' ? 4 : 49)].toFixed(3),
        name: this.name
      });
    }
  }
};

function formatOutputString(item) {
  return `${ item.name }\n Avg: ${ item.avg }, Min: ${ item.min }, Max: ${ item.max }, Med: ${ item.med }\n`;
}

function runTests(callback) {
  var outputString = "";

    runVibrateTest((values) => {
        outputString += formatOutputString(values);
        callback(outputString);
        runAckermannTest(3, 9, (values) => {
            outputString += formatOutputString(values);
            callback(outputString);
            runAudioTest((values) => {
                outputString += formatOutputString(values);
                callback(outputString);
                runWriteTest(1, (values) => {
                    outputString += formatOutputString(values);
                    callback(outputString);
                    runReadTest((values) => {
                        outputString += formatOutputString(values);
                        callback(outputString);
                        runWriteStorageTest(1, (values) => {
                            outputString += formatOutputString(values);
                            callback(outputString);
                            runReadStorageTest((values) => {
                                outputString += formatOutputString(values);
                                callback(outputString);
                            });
                        });
                    });
                });
            });
        });
    });
}

const xx = Asset.fromModule(require('./assets/s.mp3'));

function runVibrateTest(callback) {
  new Test('VibrateTest', callback, (retval) => {
    let start = Date.now();

    Vibration.vibrate();

    retval(Date.now() - start);
  }).execute();
}

function runAckermannTest(a, b, callback) {
  new Test('Ackermann', callback, (retval) => {
    let start = Date.now();

    Math.ackermann(a, b);

    retval(Date.now() - start);
  }).execute();
}

function runAudioTest(callback) {
  new Test('AudioTest', callback, async (retval) => {
    let start = Date.now();

    var soundobj = new Audio.Sound();
    await soundobj.loadAsync(xx);
    soundobj.playAsync();

    retval(Date.now() - start);
  }).execute();
}

function runWriteStorageTest (kb, callback) {
  var content = 'HoVakeVqaPkgbJSTUQJopxVLrRCMtbTHOMNOyeZiHQuyaYmsLcftJPRoDGVUaPWWECuXvQyyrkKTogsewbaIDZHukRftcCThrzSIERrbwaGZvJNQajOLQuZLivFoPdmb'.repeat(8 * kb);
  new Test('WriteStorageTest ' + kb + 'KB', callback, async (returnValue) => {
    let start = Date.now();
    for (var i = 0; i < 10; i++) {
          await SecureStore.setItemAsync('test', 'ABCDABCDABCDABCD');
    }
    returnValue(Date.now() - start);
  }).execute();
}

function runReadStorageTest (callback) {
  new Test('ReadStorageTest', callback, async (returnValue) => {
    let start = Date.now();
for (var i = 0; i < 10; i++) {
   await SecureStore.getItemAsync('test');
}
    returnValue(Date.now() - start);
  }).execute();
}

function runWriteTest (kb, callback) {
  var content = 'HoVakeVqaPkgbJSTUQJopxVLrRCMtbTHOMNOyeZiHQuyaYmsLcftJPRoDGVUaPWWECuXvQyyrkKTogsewbaIDZHukRftcCThrzSIERrbwaGZvJNQajOLQuZLivFoPdmb'.repeat(8 * kb);
  new Test('WriteFileTest ' + kb + 'KB', callback, async (returnValue) => {
    let start = Date.now();
for (var i = 0; i < 1000; i++) {
    await FileSystem.writeAsStringAsync(FileSystem.documentDirectory + 'file.txt', 'ABCDABCDABCDABCD');
}
    returnValue(Date.now() - start);
  }).execute();
}

function runReadTest (callback) {
  new Test('ReadFileTest', callback, async (returnValue) => {
    let start = Date.now();
for (var i = 0; i < 1000; i++) {
    await FileSystem.readAsStringAsync(FileSystem.documentDirectory + 'file.txt');
}
    returnValue(Date.now() - start);
  }).execute();
}
