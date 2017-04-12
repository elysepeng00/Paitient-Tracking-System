package com.yujie.hwsensorsimulator;

import android.content.Context;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.hardware.SensorManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    private SensorManagerSimulator mSensorManager = null;

    private TextView mTvAcc = null, mTVMagne = null, mTvOrien = null, mTvGyro = null, isFall = null;

    private SensorEventListener mAccListener = null,mMagListener = null , mOrienListener = null,mGyroListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvAcc = (TextView) findViewById(R.id.textViewAcc);
        mTvOrien = (TextView) findViewById(R.id.textViewOrien);
        isFall = (TextView) findViewById(R.id.textViewIsFall);
        Button buttonSend = (Button)findViewById(R.id.send);
        buttonSend.setOnClickListener(buttonSendOnClickListener);

        mSensorManager = SensorManagerSimulator.getSystemService(this, Context.SENSOR_SERVICE);

        mAccListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                mTvAcc.setText(x + ", " + y + ", " + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mOrienListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                mTvOrien.setText(sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mSensorManager.connectSimulator();

        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
        Button.OnClickListener buttonSendOnClickListener = new Button.OnClickListener() {
            public void onClick(View arg0) {
                Socket socket = null;
                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;

                try {
                    socket = new Socket("10.0.2.2", 6000);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF(mTvAcc.getText().toString());
                    boolean result = dataInputStream.readBoolean();
                    isFall.setText(result ? "Yes" : "No");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };



    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mOrienListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


}





