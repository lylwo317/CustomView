package com.kevin.mycircleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kevin.mycircleprogress.view.CompassDialView;
import com.kevin.mycircleprogress.view.DriveScoreCircleProgress;
import com.kevin.mycircleprogress.view.DriveSpeedDialView;
import com.kevin.mycircleprogress.view.SensorDialView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DriveScoreCircleProgress driverScoreCircleProgress;
    private Button btnSetScore;
    private EditText editText;
    private TextView textView;
    private RelativeLayout rlyt;

    private DriveSpeedDialView driveSpeedDialView;
    private SensorDialView driveSensorDialView;
    private CompassDialView driveCompassDialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        driverScoreCircleProgress = (DriveScoreCircleProgress) findViewById(R.id.driver_score_circle_progress);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView4);
        driveSpeedDialView = (DriveSpeedDialView) findViewById(R.id.drive_speed_dial_view);
        driveSensorDialView = (SensorDialView) findViewById(R.id.drive_gravity_dial_view);
        driveCompassDialView = (CompassDialView) findViewById(R.id.drive_compass_dial_view);



        textView.setText(getString(R.string.my_journey_time_and_pos));

        final Random ran =new Random(System.currentTimeMillis());

        btnSetScore = (Button) findViewById(R.id.btn_set_score);

        btnSetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //driverScoreCircleProgress.setScore(Float.parseFloat(editText.getText().toString()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i <= 10000000; i++) {
                            final int finalI = i;
                            final int finalI1 = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //driveCompassDialView.setRotate(ran.nextFloat() * 360f);
                                    driveSpeedDialView.setCurrentTime(finalI1*3600000);
                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

    }
}
