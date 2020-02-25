package ca.yorku.eecs.mcalcpro;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class EntryForm extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener

{

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mortgage_pro_layout);
        this.tts = new TextToSpeech(this,this);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    //use hint attribute on the plain text box
    //install the i2c library, put the jar file under the MPro/app/libs but don't extract it
    //API is already a model so no need to create another file(can just put in one file instead)
    //If you want to format all the code line, under Code --> Reformat Code

    public void buttonClicked(View v)
    {
        // int compareAmort = Integer.parseInt(mp.getAmortization());
        // int comparePrinciple = Integer.parseInt(mp.getPrinciple());

        try{

            MPro mp = new MPro();
            mp.setPrinciple(((EditText) findViewById(R.id.pBox)).getText().toString());
            mp.setAmortization(((EditText) findViewById(R.id.aBox)).getText().toString());
            mp.setInterest(((EditText) findViewById(R.id.iBox)).getText().toString());

            String s = "Monthly Payment = " + mp.computePayment("%,.2f");
            s += "\n\n";
            s += "By making this payments monthly for " + mp.getAmortization() + " years, the mortgage will be paid in full. But if you terminate the mortgage on its nth anniversary, the balance still owing depends on n as shown below:";
            s += "\n\n";
            s += String.format("%8d", 0) + mp.outstandingAfter(0, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 1) + mp.outstandingAfter(1, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 2) + mp.outstandingAfter(2, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 3) + mp.outstandingAfter(3, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 4) + mp.outstandingAfter(4, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 5) + mp.outstandingAfter(5, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 10) + mp.outstandingAfter(10, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 15) + mp.outstandingAfter(15, "%, 16.0f");
            s += "\n\n";
            s += String.format("%8d", 20) + mp.outstandingAfter(20, "%, 16.0f");
            s += "\n\n";
            s += "\n\n";


            ((TextView) findViewById(R.id.output)).setText(s);

            String s1 = "Monthly Payment = " + mp.computePayment("%,.2f");
            tts.speak(s1, TextToSpeech.QUEUE_FLUSH,null);

        } catch (Exception e){

            Toast label = Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT);
            label.setGravity(Gravity.CENTER,0,0);
            label.show();
        }
    }


    public void onSensorChanged(SensorEvent event) { //this method will be invoked whenever the device is shaken

        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if(a > 20){
            ((EditText)findViewById(R.id.pBox)).setText("");
            ((EditText)findViewById(R.id.aBox)).setText("");
            ((EditText)findViewById(R.id.iBox)).setText("");
            ((TextView)findViewById(R.id.output)).setText("");
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onInit(int initStatus) {
        //enable us to query the available languages and choose the one we want
        //Not all languages are supported on every device
        this.tts.setLanguage(Locale.US);
    }
}