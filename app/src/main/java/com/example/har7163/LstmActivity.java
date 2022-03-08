//Kaiyi Zhang (300070775)

package com.example.har7163;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.List;

public class LstmActivity extends AppCompatActivity implements SensorEventListener {


    private TextView WALKING_TextView, WALKING_UPSTAIRS_TextView, WALKING_DOWNSTAIRS_TextView, SITTING_TextView, STANDING_TextView, LAYING_TextView;

    private static final int TIME_STAMP = 128;

    //sensor data of accelerometer (body_acc and total_acc)
    private static List<Float> b_ax, b_ay, b_az;
    private static  List<Float> t_ax, t_ay, t_az;
    // sensor data of gyroscope
    private static List<Float> gx, gy, gz;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyroscope, mLinearAcceleration;

    private Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lstm);

        b_ax = new ArrayList<>(); b_ay = new ArrayList<>(); b_az = new ArrayList<>();
        t_ax = new ArrayList<>(); t_ay = new ArrayList<>(); t_az = new ArrayList<>();
        gx = new ArrayList<>(); gy = new ArrayList<>(); gz = new ArrayList<>();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mLinearAcceleration, SensorManager.SENSOR_DELAY_GAME);

        //create the tf_lite object, loaded from the saved file
        try{
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        initializeTextViews();

    }

    private void initializeTextViews() {
        WALKING_TextView = findViewById(R.id.WALKING_TextView);
        WALKING_UPSTAIRS_TextView = findViewById(R.id.WALKING_UPSTAIRS_TextView);
        WALKING_DOWNSTAIRS_TextView = findViewById(R.id.WALKING_DOWNSTAIRS_TextView);
        SITTING_TextView  = findViewById(R.id.SITTING_TextView);
        STANDING_TextView = findViewById(R.id.STANDING_TextView);
        LAYING_TextView = findViewById(R.id.LAYING_TextView);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            t_ax.add(sensorEvent.values[0]);
            t_ay.add(sensorEvent.values[1]);
            t_az.add(sensorEvent.values[2]);
        }

        else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            b_ax.add(sensorEvent.values[0]);
            b_ay.add(sensorEvent.values[1]);
            b_az.add(sensorEvent.values[2]);
        }

        else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gx.add(sensorEvent.values[0]);
            gy.add(sensorEvent.values[1]);
            gz.add(sensorEvent.values[2]);
        }
        // check if the data collected can be used to predict the activity
        if (b_ax.size() >= TIME_STAMP && b_ay.size() >= TIME_STAMP && b_az.size() >= TIME_STAMP
                && t_ax.size() >= TIME_STAMP && t_ay.size() >= TIME_STAMP && t_az.size() >= TIME_STAMP
                && gx.size() >= TIME_STAMP && gy.size() >= TIME_STAMP && gz.size() >= TIME_STAMP) {

            // The input of the LSTM
            List<Float> data=new ArrayList<>();

            data.addAll(b_ax.subList(0,TIME_STAMP));
            data.addAll(b_ay.subList(0,TIME_STAMP));
            data.addAll(b_az.subList(0,TIME_STAMP));

            data.addAll(gx.subList(0,TIME_STAMP));
            data.addAll(gy.subList(0,TIME_STAMP));
            data.addAll(gz.subList(0,TIME_STAMP));

            data.addAll(t_ax.subList(0,TIME_STAMP));
            data.addAll(t_ay.subList(0,TIME_STAMP));
            data.addAll(t_az.subList(0,TIME_STAMP));

            giveTheResult(data);

            // clean the data, a new collection round start
            data.clear();
            b_ax.clear(); b_ay.clear(); b_az.clear();
            t_ax.clear();t_ay.clear(); t_az.clear();
            gx.clear(); gy.clear(); gz.clear();
        }


    }
    // The method that is used to invoke the tf_lite model to predict the activity
    private void giveTheResult(List<Float> data){

        float[][]two_dim_array = toFloatTwoDimArray(data);

        float [][][] input = new float[1][128][9];
        input[0] = two_dim_array;

        float [][] prediction_results = new float[1][6];

        // run the tf_lite model, the output will be written in prediction_results
        tflite.run(input, prediction_results);

        WALKING_TextView.setText("Walking: \t" + String.format("%.2f", prediction_results[0][0]).toString());
        WALKING_UPSTAIRS_TextView.setText("Upstairs: \t" + String.format("%.2f", prediction_results[0][1]).toString());
        WALKING_DOWNSTAIRS_TextView.setText("Downstairs: \t" + String.format("%.2f", prediction_results[0][2]).toString());
        SITTING_TextView.setText("Sitting: \t" + String.format("%.2f", prediction_results[0][3]).toString());
        STANDING_TextView.setText("Standing: \t" + String.format("%.2f", prediction_results[0][4]).toString());
        LAYING_TextView.setText("Laying: \t" + String.format("%.2f", prediction_results[0][5]).toString());

    }


    private float[][] toFloatTwoDimArray(List<Float> data) {
//      convert data list to a two dimensional array (128, 9)
        float[][] array =new float[TIME_STAMP][9];
        for (int counter = 0; counter < data.size(); counter++) {
            int integer_division = counter / TIME_STAMP;
            int remainder = counter % TIME_STAMP;
            Float value = data.get(counter);
            array[remainder][integer_division] = (value != null ? value: Float.NaN);
        }
        return array;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("lstm.tflite");
        FileInputStream inputStream = new  FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mLinearAcceleration, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
}