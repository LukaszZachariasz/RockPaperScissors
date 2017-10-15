package com.pixelquick.luc.marynarz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel1);
        TextView bestScoreLabel = (TextView) findViewById(R.id.bestScoreLabel1);
        TextView endGameLabel1 = (TextView) findViewById(R.id.endGameLabel11);
        TextView endGameLabel2 = (TextView) findViewById(R.id.endGameLabel22);


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

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        return;
    }


    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
        return;

        //
    }
}