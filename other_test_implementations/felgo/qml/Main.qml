import Felgo 3.0
import QtQuick 2.0
import QtMultimedia 5.9

App {
    id: app
  NavigationStack {
    Page {
      title: "My First App"

      AppButton {
        text: "Run tests"
        flat: false
        anchors.horizontalCenter: parent.horizontalCenter
        onClicked: {
            runTests();
        }
      }

      Text {
          id: content
          text: "Lolool"
          anchors.horizontalCenter: parent.horizontalCenter
      }

      Audio {
          id: aud
          source: "https://felgo.com/web-assets/beep.mp3"
      }
    }
  }

  function runTest (label, func, repeats, exit) {
      console.log('RUNNING TEST ' + label + ' : ' + repeats);
      var values = [];
      for (var i = 0; i < repeats; i++) {
          console.log('EXEC ' + values.length);
          values.push(func());
      }

      if (exit) {
          exit();
      }

      values.sort((a, b) => a - b);
      var min = values[0];
      var max = values[repeats - 1];
      var avg = values.reduce((a, b) => a + b, 0) / repeats;
      var med = repeats % 2 == 1 ? values[repeats / 2] : ((values[repeats / 2] + values[repeats / 2 - 1]) / 2);

      content.text += `${label}\nMin: ${min}, Max: ${max}, Avg: ${avg}, Med: ${med}\n`;
  }

   function ack(m, n)
  {
      return m === 0 ? n + 1 : ack(m - 1, n === 0 ? 1 : ack(m, n - 1));
  }

  function runTests () {
      content.text = "READY";
      var str = 'ABCDABCDABCDABCD';

      console.log('STARTING');

      runTest('Prefs Write 1000 x 1K', () => {
        var old = Date.now();
        for (var i = 0; i < 100; i++) app.settings.setValue('ab', str);
        return (Date.now() - old);
      }, 100);

      runTest('Prefs Read 1000 x 1K', () => {
        var old = Date.now();
        for (var i = 0; i < 100; i++) var x = app.settings.getValue('ab');
        return (Date.now() - old);
      }, 100);

      runTest('File Write 1000 x 1K', () => {
        var old = Date.now();
        for (var i = 0; i < 100; i++) if (!fileUtils.writeFile(Qt.resolvedUrl('ab.txt'), str)) console.log('Error');
        return (Date.now() - old);
      }, 100);

      runTest('File Read 1000 x 1K', () => {
        var old = Date.now();
        for (var i = 0; i < 100; i++) if (fileUtils.readFile(Qt.resolvedUrl('ab.txt')) == null) console.log('Error');
        return (Date.now() - old);
      }, 100);

  }
}
