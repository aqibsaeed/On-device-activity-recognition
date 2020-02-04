package org.tensorflow.lite.examples.transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.tensorflow.lite.examples.transfer.api.TransferLearningModel.Prediction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.os.Vibrator;
import android.widget.Toast;

enum Mode {
  Data_Collection,
  Inference,
  Training

}

public class MainActivity extends AppCompatActivity implements SensorEventListener  {

  final int NUM_SAMPLES = 400;
  String MODEL_NAME = "ar_model";
  double VB_THRESHOLD = 0.75;

  int classAInstanceCount = 0;
  int classBInstanceCount = 0;
  boolean isRunning = false;

  SensorManager mSensorManager;
  Sensor mAccelerometer;
  Sensor mGyroscope;
  TransferLearningModelWrapper tlModel;
  String classId;
  static List<Float> x_accel;
  static List<Float> y_accel;
  static List<Float> z_accel;
  static List<Float> x_gyro;
  static List<Float> y_gyro;
  static List<Float> z_gyro;

  static List<Float> input_signal;

  Mode mode;

  Button startButton;
  Button stopButton;
  TextView classATextView;
  TextView classBTextView;
  TextView classAInstanceCountTextView;
  TextView classBInstanceCountTextView;
  TextView lossValueTextView;
  Spinner optionSpinner;
  Spinner classSpinner;
  Vibrator vibrator;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    startButton = (Button) findViewById(R.id.buttonStart);
    stopButton = (Button) findViewById(R.id.buttonStop);
    stopButton.setEnabled(false);

    classATextView = (TextView)findViewById(R.id.classAOutputValueTextView);
    classBTextView = (TextView)findViewById(R.id.classBOutputValueTextView);
    classAInstanceCountTextView = (TextView)findViewById(R.id.classACountValueTextView);
    classBInstanceCountTextView = (TextView)findViewById(R.id.classBCountValueTextView);
    lossValueTextView = (TextView)findViewById(R.id.lossValueTextView);

    optionSpinner = (Spinner) findViewById(R.id.optionSpinner);
    classSpinner = (Spinner) findViewById(R.id.classSpinner);

    ArrayAdapter<CharSequence> optionAdapter = ArrayAdapter
            .createFromResource(this, R.array.options_array,
                    R.layout.spinner_item);
    optionAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    optionSpinner.setAdapter(optionAdapter);

    ArrayAdapter<CharSequence> classAdapter = ArrayAdapter
            .createFromResource(this, R.array.class_array,
                    R.layout.spinner_item);
    classAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    classSpinner.setAdapter(classAdapter);

    x_accel = new ArrayList<Float>();
    y_accel = new ArrayList<Float>();
    z_accel = new ArrayList<Float>();
    x_gyro= new ArrayList<Float>();
    y_gyro = new ArrayList<Float>();
    z_gyro = new ArrayList<Float>();
    input_signal = new ArrayList<Float>();

    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    tlModel = new TransferLearningModelWrapper(getApplicationContext());

    optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
                                 int position, long id) {
        String option = (String) parent.getItemAtPosition(position);
        switch (option){
          case "Data Collection":
            mode = Mode.Data_Collection;
            break;
          case "Training":
            mode = Mode.Training;
            break;
          case "Inference":
            mode = Mode.Inference;
            break;
          default:
            throw new IllegalArgumentException("Invalid app mode.");
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
      }
    });

    classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
                                 int position, long id) {
        classId = (String) parent.getItemAtPosition(position);

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
      }
    });

    startButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        optionSpinner.setEnabled(false);
        isRunning = true;
        // Uncomment following lines to load an existing model.
       /*     if(mode == Mode.Inference){
              File modelPath = getApplicationContext().getFilesDir();
              File modelFile = new File(modelPath, MODEL_NAME);
              if(modelFile.exists()){
                tlModel.loadModel(modelFile);
                Toast.makeText(getApplicationContext(), "Model loaded.", Toast.LENGTH_SHORT).show();
              }
            }
       */
      }
    });

    stopButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        optionSpinner.setEnabled(true);
        isRunning = false;
        if(mode == Mode.Training){
          tlModel.disableTraining();
          // Uncomment following lines to save the model.
          /*
            File modelPath = getApplicationContext().getFilesDir();
            File modelFile = new File(modelPath, MODEL_NAME);
            tlModel.saveModel(modelFile);
            Toast.makeText(getApplicationContext(), "Model saved.", Toast.LENGTH_SHORT).show();
           */
        }
      }
    });
  }

  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

  protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
  }

  protected void onDestroy() {
    super.onDestroy();
    tlModel.close();
    tlModel = null;
    mSensorManager = null;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    switch (event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        x_accel.add(event.values[0]); y_accel.add(event.values[1]); z_accel.add(event.values[2]);
        break;
      case Sensor.TYPE_GYROSCOPE:
        x_gyro.add(event.values[0]); y_gyro.add(event.values[1]); z_gyro.add(event.values[2]);
        break;
    }

    //Check if we have desired number of samples for sensors, if yes, the process input.
    if(x_accel.size() == NUM_SAMPLES && y_accel.size() == NUM_SAMPLES &&
            z_accel.size() == NUM_SAMPLES && x_gyro.size() == NUM_SAMPLES &&
            y_gyro.size() == NUM_SAMPLES && z_gyro.size() == NUM_SAMPLES)
      processInput();
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }

  private void processInput()
  {
      int i = 0;
      while (i < NUM_SAMPLES) {
        input_signal.add(x_accel.get(i));
        input_signal.add(y_accel.get(i));
        input_signal.add(z_accel.get(i));
        input_signal.add(x_gyro.get(i));
        input_signal.add(y_gyro.get(i));
        input_signal.add(z_gyro.get(i));
        i++;
      }

      float[] input = toFloatArray(input_signal);

      if (isRunning){
        if(mode == Mode.Training){
          int batchSize = tlModel.getTrainBatchSize();
          if(classAInstanceCount >= batchSize && classBInstanceCount >= batchSize){
            tlModel.enableTraining((epoch, loss) -> runOnUiThread(new Runnable() {
              @Override
              public void run() {
                lossValueTextView.setText(Float.toString(loss));
              }
            }));
          }
          else{
            String message = batchSize + " instances per class are required for training.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            stopButton.callOnClick();
          }

        }
        else if (mode == Mode.Data_Collection){
          tlModel.addSample(input, classId);

          if (classId.equals("Class A")) classAInstanceCount += 1;
          else if(classId.equals("Class B")) classBInstanceCount += 1;

          classAInstanceCountTextView.setText(Integer.toString(classAInstanceCount));
          classBInstanceCountTextView.setText(Integer.toString(classBInstanceCount));
        }
        else if (mode == Mode.Inference) {
          Prediction[] predictions = tlModel.predict(input);
          // Vibrate the phone if Class B is detected.
          if(predictions[1].getConfidence() > VB_THRESHOLD)
            vibrator.vibrate(VibrationEffect.createOneShot(200,
                    VibrationEffect.DEFAULT_AMPLITUDE));

          String classAOutput = Float.toString(round(predictions[0].getConfidence(), 4));
          String classBOutput = Float.toString(round(predictions[1].getConfidence(), 4));

          classATextView.setText(classAOutput);
          classBTextView.setText(classBOutput);
        }
      }

      // Clear all the values
      x_accel.clear(); y_accel.clear(); z_accel.clear();
      x_gyro.clear(); y_gyro.clear(); z_gyro.clear();
      input_signal.clear();
  }

  private float[] toFloatArray(List<Float> list)
  {
    int i = 0;
    float[] array = new float[list.size()];

    for (Float f : list) {
      array[i++] = (f != null ? f : Float.NaN);
    }
    return array;
  }

  private static float round(float d, int decimalPlace) {
    BigDecimal bd = new BigDecimal(Float.toString(d));
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return bd.floatValue();
  }
}
