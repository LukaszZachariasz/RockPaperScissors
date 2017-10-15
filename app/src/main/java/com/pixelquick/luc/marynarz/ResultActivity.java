package com.pixelquick.luc.marynarz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_result);

        MediaPlayer gameOverPlayer = MediaPlayer.create(ResultActivity.this, R.raw.over);
        MediaPlayer newRecordPlayer = MediaPlayer.create(ResultActivity.this, R.raw.record);


        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView bestScoreLabel = (TextView) findViewById(R.id.bestScoreLabel);
        TextView endGameLabel1 = (TextView) findViewById(R.id.endGameLabel1);
        TextView endGameLabel2 = (TextView) findViewById(R.id.endGameLabel2);

        Button startButton = (Button) findViewById(R.id.repeatLabel);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font8.ttf");

        scoreLabel.setTextSize(60);
        scoreLabel.setTypeface(custom_font);
        scoreLabel.setTextColor(Color.RED);
        bestScoreLabel.setTextSize(30);
        bestScoreLabel.setTextColor(Color.BLUE);
        bestScoreLabel.setTypeface(custom_font);
        endGameLabel1.setTextSize(40);
        endGameLabel1.setTextColor(Color.BLUE);
        endGameLabel1.setTypeface(custom_font);
        endGameLabel2.setTextSize(40);
        endGameLabel2.setTextColor(Color.GREEN);
        endGameLabel2.setTypeface(custom_font);

        startButton.setTypeface(custom_font);
        startButton.setTextSize(25);
        startButton.setTextColor(Color.RED);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        int bestScore = settings.getInt("BEST_SCORE", 0);

        if (score > bestScore) {

            bestScoreLabel.setText("" + score);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("BEST_SCORE", score);
            editor.commit();
            newRecordPlayer.start();

        } else {
            gameOverPlayer.start();
            bestScoreLabel.setText("" + bestScore);
        }
    }

    @Override
    public void onBackPressed() {
        //Disabled back button
        finish();
    }

    public void start(View view) {
        try {

            Thread.sleep(200);
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0, 0);

        } catch (InterruptedException e) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        return;
    }
}
