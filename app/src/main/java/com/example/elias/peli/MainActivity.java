package com.example.elias.peli;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    private ToggleButton easyButton;
    private ToggleButton mediumButton;
    private ToggleButton hardButton;
    private Button playButton;



    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH  = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.LEFT_JUSTIFY = (int) dm.widthPixels/15;

        setContentView(R.layout.activity_main);
        //setContentView(new GamePanel(this));

        easyButton = (ToggleButton) findViewById(R.id.easy_button);
        mediumButton = (ToggleButton) findViewById(R.id.medium_button);
        hardButton = (ToggleButton) findViewById(R.id.hard_button);
        playButton = (Button) findViewById(R.id.play_button);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();

            }
        }) ;



         CompoundButton.OnCheckedChangeListener changeChecker = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView != easyButton) {
                        easyButton.setChecked(false);
                        Constants.GAME_SPEED = 4000.0f;

                    }
                    if (buttonView != mediumButton) {
                        mediumButton.setChecked(false);
                        Constants.GAME_SPEED = 10000.0f;
                    }
                    if (buttonView != hardButton) {
                        hardButton.setChecked(false);
                        Constants.GAME_SPEED = 15000.0f;



                    }
                }
            }
        };
        easyButton.setOnCheckedChangeListener(changeChecker);

        mediumButton.setOnCheckedChangeListener(changeChecker);

        hardButton.setOnCheckedChangeListener(changeChecker);


        /*
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(v);

            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(v);

            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonSelected(v);
            }
         });
        */
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyButton.isChecked()|| mediumButton.isChecked() || hardButton.isChecked())
                        startGame();
                else
                    showToast("Valitse vaikeustaso");

            }
        });
    }
    private void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
    public void setButtonSelected(View v) {
        switch (v.getId()) {

        }
    }
    private void startGame() {
        setContentView(new GamePanel(this));
    }
}







