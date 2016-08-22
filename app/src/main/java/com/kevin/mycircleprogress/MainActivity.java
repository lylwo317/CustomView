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

        rlyt = (RelativeLayout) findViewById(R.id.rlyt);
        rlyt.setBackground(new ListViewDrawable());

        btnSetScore = (Button) findViewById(R.id.btn_set_score);

        btnSetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverScoreCircleProgress.setScore(Float.parseFloat(editText.getText().toString()));
            }
        });

    }
}
