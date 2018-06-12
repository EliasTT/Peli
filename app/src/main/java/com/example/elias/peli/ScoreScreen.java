package com.example.elias.peli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScoreScreen extends Activity {
    @Override
    protected void onCreate (Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        Intent i = getIntent();
        String score = i.getStringExtra("Score");
        setContentView(R.layout.results);

        TextView scoreText = (TextView) findViewById(R.id.score_text);
        TextView highscoreText = (TextView) findViewById(R.id.highscore_text);

        Button returnButton = (Button) findViewById(R.id.return_button);

        int scoreT = getIntent().getIntExtra("Score",0);
        scoreText.setText(scoreT + "");

        SharedPreferences settings = getSharedPreferences("Game_data", Context.MODE_PRIVATE);
        int highscore = settings.getInt("High_score", 0);

        if (scoreT >highscore) {
            highscoreText.setText("High score : " + highscore);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("High_score",scoreT);
            editor.commit();
        } else {
            highscoreText.setText("High score : " + highscore);
        }
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                returnMenu();
            }
        });
    }
    public void returnMenu() {
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
    }
}


