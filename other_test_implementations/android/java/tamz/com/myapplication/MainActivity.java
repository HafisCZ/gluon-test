package tamz.com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TEMP;
    static {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append("XExySdG2i95moEBwKfYa0YJsjEU9krPbfxpoyZcIKbZpvonwxIi3xIeh1qMGJbPsCCAjCXrOmRB3zaPPrcnSmPkkN3b05pNT6JQgiuiv7VdJEHkWoxkKml1mAYtsBN95");
        }
        TEMP = stringBuilder.toString();
        TEMP = "ABCDABCDABCDABCD";
    }

    public Test testFileWrite (String string, int loops) {
        return new Test((accept) -> {
            long begin = System.nanoTime();
            for (int i = 0; i < loops; i++) {
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.getApplicationContext().openFileOutput("a.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(string);
                    outputStreamWriter.close();
                    this.getApplicationContext().deleteFile("a.txt");
                } catch (Exception e) {
                    accept.accept(e.getMessage());
                }
            }
            accept.accept(System.nanoTime() - begin);
        }).repeat(100);
    }

    public Test testFileRead (String string, int loops) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.getApplicationContext().openFileOutput("a.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(string);
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Test((accept) -> {
            long begin = System.nanoTime();
            for (int i = 0; i < loops; i++) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(this.getApplicationContext().openFileInput("a.txt"));
                    String str = inputStreamReader.toString();
                    inputStreamReader.close();
                } catch (Exception e) {
                    accept.accept(e.getMessage());
                }
            }
            accept.accept(System.nanoTime() - begin);
        }).onExit(() -> {
            this.getApplicationContext().deleteFile("a.txt");
        }).repeat(100);
    }

    public Test testPreferencesWrite (String string, int loops) {

        return new Test((accept) -> {
            long begin = System.nanoTime();

            for (int i = 0; i < loops; i++) {
                SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
                pref.edit().putString("a", string).commit();
               // pref.edit().remove("a").commit();
            }
            accept.accept(System.nanoTime() - begin);
        }).repeat(100);
    }

    public Test testPreferencesRead (String string, int loops) {
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);

        pref.edit().putString("a", string).commit();
        return new Test((accept) -> {
            long begin = System.nanoTime();

            for (int i = 0; i < loops; i++) {
                SharedPreferences b = this.getPreferences(Context.MODE_PRIVATE);
                String s = b.getString("a", "");
            }
            accept.accept(System.nanoTime() - begin);
        }).onExit(() -> {
            SharedPreferences a = this.getPreferences(Context.MODE_PRIVATE);
            a.edit().remove("a").commit();
        }).repeat(100);
    }

    public Test testVibration () {
        return new Test((accept) -> {
           long begin = System.nanoTime();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
           accept.accept(System.nanoTime() - begin);
        }).repeat(100);
    }

    public Test testAudioPlay (int mid) {
        return new Test((accept) -> {
            long begin = System.nanoTime();
            MediaPlayer mp = MediaPlayer.create(this, mid);
            mp.start();
            accept.accept(System.nanoTime() - begin);
        }).repeat(100);
    }

    public Test testGeolocation () {
        return new Test((accept) -> {
            long begin = System.nanoTime();
            try {
                LocationManager mLocationManager = (LocationManager) this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (SecurityException e) {
                accept.accept(e.getMessage());
            }
            accept.accept(System.nanoTime() - begin);
        }).repeat(10);
    }

    public Test testAckermann (int a, int b) {
        return new Test((accept) -> {
            long begin = System.nanoTime();
            Test.Ackermann(a, b);
            accept.accept(System.nanoTime() - begin);
        }).repeat(100);
    }

    private final DecimalFormat df = new DecimalFormat("#.###");

    public void print(String name, Test.TestResults results) {
        String s = name + "\nmin: " + df.format(results.min / 1E6) + " max: " + df.format(results.max / 1E6) + "\navg: " + df.format(results.average / 1E6) + " med: " + df.format(results.median / 1E6);
        textView.post(() -> textView.setText(textView.getText() + "\n" + s));
    }

    public void progress(List<Double> l, int d) {
        progressBar.post(() -> progressBar.setProgress((int)(100.0 * ((double) l.size()) / ((double) d))));
    }

    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.test);
        progressBar = findViewById(R.id.bar);

        Button button = findViewById(R.id.button);
        button.setOnClickListener((View v) -> {
            ThreadGroup group = new ThreadGroup("threadGroup");
            new Thread(group, this::runTests, "myThread", 2 * 1024 * 1024).start();
        });
    }

    private void runTests() {
        textView.post(() -> textView.setText(""));
        testPreferencesWrite(TEMP, 1000).onFinished((Test.TestResults a) -> {
            print("PrefsWrite 1000 @ 1KB (100 samples)", a);
        }).onProgress(this::progress).execute();
        testPreferencesRead(TEMP, 1000).onFinished((Test.TestResults b) -> {
            print("PrefsRead 1000 @ 1KB (100 samples)", b);
        }).onProgress(this::progress).execute();
        testFileWrite(TEMP, 1000).onFinished((Test.TestResults c) -> {
            print("FileWrite 100 @ 1KB (100 samples)", c);
        }).onProgress(this::progress).execute();
        testFileRead(TEMP, 1000).onFinished((Test.TestResults d) -> {
            print("FileRead 100 @ 1KB (100 samples)", d);
        }).onProgress(this::progress).execute();
        testGeolocation().onFinished((Test.TestResults e) -> {
            print("Geolocation (10 samples)", e);
        }).onProgress(this::progress).execute();
        testVibration().onFinished((Test.TestResults f) -> {
            print("Vibration (100 samples)", f);
        }).onProgress(this::progress).execute();
        testAudioPlay(R.raw.s).onFinished((Test.TestResults g) -> {
            print("AudioPlay (100 samples)", g);
        }).onProgress(this::progress).execute();
        testAckermann(3, 9).onFinished((Test.TestResults h) -> {
            print("Ackermann 3/9 (100 samples)", h);
        }).onProgress(this::progress).execute();
        testAckermann(3, 11).onFinished((Test.TestResults i) -> {
            print("Ackermann 3/11 (100 samples)", i);
        }).onProgress(this::progress).execute();
    }
}
