package com.hari.utilitycompass;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends BaseActivity implements SensorEventListener {

    AppCompatActivity activity;
    ImageView Instruct;
    String mins = "", secs = "", dir = "";
    int backButtonCount = 0, minss = 0, secss = 0;
    TextView tvHeading, _tvMin, _tvSec, tvDir;
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_compass);
//        initToolBar(activity, "My Compass", statusColor, themeColor, false);

//    	----------------------hhari-----------
        Instruct = (ImageView) findViewById(R.id.btn_instruct);
        Instruct.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//      				shareMethod();
                Intent intent = new Intent(getApplicationContext(), InstructActivity.class);
                startActivity(intent);
            }
        });
//    	    	----------------------hhari-----------
        //for initial delay..

/////////////hari_experiment////////////////
//          	m_laps.setOnItemClickListener(new OnItemClickListener() {
//        	    @Override
//        	    public void onItemClick(AdapterView<?> list, View view, int position, long id) {
//        	        Log.i(TAG, "onListItemClick: " + position);
//
//        	        }
//
//        	    }
//        	});

////////////////////////////////////////////////////////////////////////////              
        image = (ImageView) findViewById(R.id.imageViewCompass);

//		Typeface font = Typeface.createFromAsset(getAssets(),"fonts/TheReduxRegular.ttf");

        tvHeading = (TextView) findViewById(R.id.tvHeading);
        _tvMin = (TextView) findViewById(R.id.tvMin);
        _tvSec = (TextView) findViewById(R.id.tvSec);
        tvDir = (TextView) findViewById(R.id.tvDir);

//		tvHeading.setTypeface(font);
//		_tvMin.setTypeface(font);
//		_tvSec.setTypeface(font);
//		tvDir.setTypeface(font);

//        tvHeading.setTextColor(Color.MAGENTA);		
//        _tvMin.setTextColor(Color.MAGENTA);		
//        _tvSec.setTextColor(Color.MAGENTA);
//		tvDir.setTextColor(Color.MAGENTA);	

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager.equals(null)) {
            Toast.makeText(getApplicationContext(), "Sensor is not found.", Toast.LENGTH_SHORT).show();
        }
    }//endharioncr

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);

        float degreee = (event.values[0]);
        float deg_new = Math.round(degree);

//////////////////////////////////////////////////////////////////////////////////////
        String cal_deg = DecDeg(degreee, 0);
//////////////////////////////////////////////////////////////////////////////////////		

        int degg = 0;
        degg = (int) degree;
//		int degg= Integer.parseInt(Float.toString(degree).toString());
//		tvHeading. setText(""+ degg + (char) 0x00B0 +" "+ cal_deg +" " + "" + dir );
        tvHeading.setText("" + hariformatDigits(degg) + (char) 0x00B0 + " ");
        _tvMin.setText("" + mins.toString());
        _tvSec.setText("" + secs.toString());
        tvDir.setText("" + dir);
//		tvHeading. setText(""+ degreee + (char) 0x00B0 +"  " + "" + dir + "\n" + cal_deg );
//		tvHeading.setText("" + (Float.toString(degree))+ (char) 0x00B0+"  "+""+dir);
//		tvHeading.setText("" + Integer.parseInt(Float.toString(degree)) + (char) 0x00B0+"  "+""+dir);

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(210);

        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degreee;

        if (degree == 0 || degree == 360) {
            dir = "N";
        } else if (degree > 0 && degree < 23) {
            dir = "N";
        } else if (degree > 22 && degree < 68) {
            dir = "NE";
        } else if (degree > 67 && degree < 113) {
            dir = "E";
        } else if (degree > 112 && degree < 158) {
            dir = "SE";
        } else if (degree > 157 && degree < 203) {
            dir = "S";
        } else if (degree > 202 && degree < 248) {
            dir = "SW";
        } else if (degree > 247 && degree < 293) {
            dir = "W";
        } else if (degree > 292 && degree < 338) {
            dir = "NW";
        } else if (degree > 337 && degree < 360) {
            dir = "N";
        }
//		else if (degree==360){dir="N";}

//		else if(degree==90){dir="East";} else if(degree==180){dir="South";} else if(degree==270){dir="West";}
//		else if(degree>0&&degree<90){dir="NE";} else if(degree>90&&degree<180){dir="ES";} else if(degree>180&&degree<270){dir="SW";}else if(degree>270&&degree<360){dir="WN";}

//		if(degree==0||degree==360){dir="North";} else if(degree==90){dir="East";} else if(degree==180){dir="South";} else if(degree==270){dir="West";}
//		else if(degree>0&&degree<90){dir="NE";} else if(degree>90&&degree<180){dir="ES";} else if(degree>180&&degree<270){dir="SW";}else if(degree>270&&degree<360){dir="WN";}

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public String DecDeg(float hari_deg, int h) {
        // Variables.
        int degrees = 0;
        double degreesTemp = 0.0;
        int minutes = 0;
        float minutesTemp = 0;
        int seconds = 0;
        float secondsTemp = 0;

        String message = "";

        boolean isNegativeAngle;

        // Get decimal number of degrees from user.
        degreesTemp = hari_deg;
//				degreesTemp = parseFloat(window.document.decDegrees.decDegrees.value);

        // Assume angle is not negative.
        isNegativeAngle = false;

        // Is the decimal number of degrees negative?
        if (degreesTemp < 0.0) {
            // Remember that the decimal number of degrees was entered as a negative number.
            isNegativeAngle = true;

            // Work with the absolute value of the decimal number of degrees.
            degreesTemp = -degreesTemp;

        }

        // Keep the integer value of degrees.
        degrees = (int) Math.floor(degreesTemp);


        // Find the decimal value of degrees.
        minutesTemp = (float) (degreesTemp - degrees);
        // Convert the decimal value of degrees into number of minutes.
        minutesTemp = (float) (60.0 * minutesTemp);
        // Keep the integer value of minutes.
        minutes = (int) Math.floor(minutesTemp);
//				minss=minutes;

        // Find the decimal value of minutes.
        secondsTemp = minutesTemp - minutes;
        // Convert the decimal value of minutes into number of seconds.
        secondsTemp = (float) (60.0 * secondsTemp);
        // Round to the integer value of seconds.
        seconds = Math.round(secondsTemp);
//				secss=seconds;

        // Create a message about if the angle is negative or a positive.
        if (isNegativeAngle) {
//					message = "This angle is negative.\n";
//					message = "";
//					System.out.println("thi;s is _+ve value.");
        } else {
//					message = "This angle is positive.\n";
//					message = "";
//					System.out.println("thi;s is --ve value.");
        }

        // Append the results to the message.
//				message += "Degrees = " + degrees + "\n";
//				message += "Minutes = " + minutes + "\n";
//				message += "Seconds = " + seconds;

//				message += "Degrees = " + degrees + "\n";
        message += minutes + "' ";
        message += seconds + "'' ";

        mins = formatDigits(minutes) + "' ";
        secs = formatDigits(seconds) + "'' ";
        // Announce the message.
        return (message);
    }

    private String formatDigits(long num) {
        return (num < 10) ? "0" + num : new Long(num).toString();

    }

    private String hariformatDigits(long num) {

        String num_hari = "";
        if (num < 10) {
            num_hari = ("00" + num).toString();
        } else if (num < 100) {
            num_hari = ("0" + num).toString();
        } else {
            num_hari = String.valueOf(num);
        }
        return num_hari;
    }

    public void onBackPressed() {
        if (backButtonCount >= 1) {
            super.onBackPressed();
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        } else {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}
