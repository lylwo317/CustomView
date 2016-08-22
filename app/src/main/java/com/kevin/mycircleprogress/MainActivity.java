package com.kevin.mycircleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kevin.mycircleprogress.drawable.ListViewDrawable;
import com.kevin.mycircleprogress.view.DriveScoreCircleProgress;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DriveScoreCircleProgress driverScoreCircleProgress;
    private Button btnSetScore;
    private EditText editText;
    private TextView textView;
    private RelativeLayout rlyt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        driverScoreCircleProgress = (DriveScoreCircleProgress) findViewById(R.id.driver_score_circle_progress);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView4);

        textView.setText(getString(R.string.my_journey_time_and_pos));

        final Random ran =new Random(System.currentTimeMillis());

        rlyt = (RelativeLayout) findViewById(R.id.rlyt);
        rlyt.setBackground(new ListViewDrawable());

        btnSetScore = (Button) findViewById(R.id.btn_set_score);

        btnSetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //driverScoreCircleProgress.setScore(Float.parseFloat(editText.getText().toString()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i <= 100; i++) {
                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    driverScoreCircleProgress.setScore(ran.nextFloat()*50f);
                                }
                            });
                            try {
                                Thread.sleep(300);
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
