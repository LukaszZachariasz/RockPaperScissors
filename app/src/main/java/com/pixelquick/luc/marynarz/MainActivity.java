package com.pixelquick.luc.marynarz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    //Widgets Handlers
    private TextView score_label;
    private TextView start_label_rock;
    private TextView start_label_paper;
    private TextView start_label_scissors;
    private ImageView player;
    private ImageView enemy1, enemy_cut;
    private ImageView enemy2, enemy_paper;
    private ImageView enemy3, enemy_rock;
    private ImageView img1, img2, img3;

    //Sizes
    private int frame_height;
    private int player_size;

    private int screen_width;
    private int screen_height;

    //Positions
    private int player_y_pos;

    private int enemy_1_x_pos;
    private int enemy_2_x_pos;
    private int enemy_3_x_pos;

    private int enemy_1_y_pos;
    private int enemy_2_y_pos;
    private int enemy_3_y_pos;

    //Score
    private int score = 0;

    //Images ID connected to enemy
    private int player_figure_id = 1;
    private int enemy_1_figure_id = 2;
    private int enemy_2_figure_id = 3;
    private int enemy_3_figure_id = 1;

    //In Anim Menu enemy X-Positions
    private int in_anim_menu_enemy_1_x_pos;
    private int in_anim_menu_enemy_2_x_pos;
    private int in_anim_menu_enemy_3_x_pos;

    //Init Classes
    private Handler game_handler = new Handler();
    private Timer game_timer = new Timer();

    private Handler y_change_pos_and_color_handler = new Handler();
    private Timer y_change_pos_and_color_timer = new Timer();

    private Handler menu_anim_handler = new Handler();
    private Timer menu_anim_timer = new Timer();

    //Status Check
    private boolean action_flag = false;
    private boolean start_flag = false;

    //Vibrator
    private Vibrator vibs;

    //Sound
    private MediaPlayer music_player;
    private MediaPlayer catch_sound_player;

    //Control
    private boolean is_in_game = true;
    private int speed_movement;

    //Result Intent
    private Intent intent_result;

    //Random
    Random rm = new Random();

    //Other variables
    int i = 0, j = 0, k = 0;
    int direction;
    boolean color = true;

    //AsyncTask Class for "Catch" sound
    /*private class PlayCatchSoundOperationAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            catch_sound_player.start();
            return null;
        }

    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        super.onCreate(null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_main);

        vibs = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Init Intent
        intent_result = new Intent(getApplicationContext(), ResultActivity.class);

        music_player = MediaPlayer.create(MainActivity.this, R.raw.music);
        catch_sound_player = MediaPlayer.create(MainActivity.this, R.raw.catchme);
       // PlayCatchSoundOperationAsync playCatchSoundOperationAsync = new PlayCatchSoundOperationAsync();

        //Load and Loop music
        music_player.start();
        music_player.setLooping(true);

        //Init and Load custom font from assets folder
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font8.ttf");

        //Init widgets
        score_label = (TextView) findViewById(R.id.scoreLabel);
        start_label_rock = (TextView) findViewById(R.id.startLabelRock);
        start_label_paper = (TextView) findViewById(R.id.startLabelPaper);
        start_label_scissors = (TextView) findViewById(R.id.startLabelScissors);

        player = (ImageView) findViewById(R.id.player);
        enemy1 = (ImageView) findViewById(R.id.paper);
        enemy2 = (ImageView) findViewById(R.id.rock);
        enemy3 = (ImageView) findViewById(R.id.cut);

        enemy_paper = (ImageView) findViewById(R.id.paper);
        enemy_rock = (ImageView) findViewById(R.id.rock);
        enemy_cut = (ImageView) findViewById(R.id.cut);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        //Init colors for labels
        start_label_rock.setTextColor(Color.GREEN);
        start_label_paper.setTextColor(Color.BLUE);
        start_label_scissors.setTextColor(Color.RED);

        //Variable with TextSize parameter
        int titleTextSize = 75;

        //Set TextSize
        start_label_rock.setTextSize(titleTextSize);
        start_label_paper.setTextSize(titleTextSize - 13);
        start_label_scissors.setTextSize(titleTextSize - 34);

        //Get custom fonts
        start_label_rock.setTypeface(custom_font);
        start_label_paper.setTypeface(custom_font);
        start_label_scissors.setTypeface(custom_font);

        //Set size and Load fonts
        score_label.setTextSize(35);
        score_label.setTypeface(custom_font);

        //Hide parts of game screen
        player.setVisibility(View.INVISIBLE);
        score_label.setVisibility(View.INVISIBLE);
        enemy1.setVisibility(View.INVISIBLE);
        enemy2.setVisibility(View.INVISIBLE);
        enemy3.setVisibility(View.INVISIBLE);

        //Show parts of start screen
        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.VISIBLE);
        img3.setVisibility(View.VISIBLE);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        //Save screen size
        screen_width = size.x;
        screen_height = size.y;

        //Calculate speed of Player widget
        speed_movement = (screen_height - screen_width) / 30;

        //Animation of start screen - Change pos every 20 milisec
        menu_anim_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                menu_anim_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        animMenu();
                    }
                });
            }
        }, 0, 20);
    }


    @Override
    protected void onResume() {
        super.onResume();
        is_in_game = true;
        music_player.start();
        music_player.setLooping(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        music_player.pause();
        is_in_game = false;
        finish();
        return;
    }

    @Override
    public void onBackPressed() {
        //Disabled back button
        //startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        overridePendingTransition(0, 0);
        finish();
        return;
    }


    public void changePos() {

        hitCheck();


        //enemy's speed calculate for different screen size
        enemy_1_x_pos -= (screen_width / 70) + 0;
        enemy_2_x_pos -= (screen_width / 70) + 2;
        enemy_3_x_pos -= (screen_width / 70) + 3;

        //enemy1 movement
        if (enemy_1_x_pos < -enemy1.getWidth()) {
            enemy_1_x_pos = screen_width + player_size;
            enemy_1_y_pos = (int) Math.floor(Math.random() * (frame_height - enemy1.getHeight()));
        }

        enemy1.setX(enemy_1_x_pos);
        enemy1.setY(enemy_1_y_pos);

        //enemy2 movement
        if (enemy_2_x_pos < -enemy2.getWidth()) {
            enemy_2_x_pos = screen_width + player_size;
            enemy_2_y_pos = (int) Math.floor(Math.random() * (frame_height - enemy2.getHeight()));
        }

        enemy2.setX(enemy_2_x_pos);
        enemy2.setY(enemy_2_y_pos);

        //enemy3 movement
        if (enemy_3_x_pos < -enemy3.getWidth()) {
            enemy_3_x_pos = screen_width + player_size;
            enemy_3_y_pos = (int) Math.floor(Math.random() * (frame_height - enemy3.getHeight()));
        }

        enemy3.setX(enemy_3_x_pos);
        enemy3.setY(enemy_3_y_pos);

        //Touching of Human xD
        if (action_flag) {
            player_y_pos -= speed_movement;
        } else {
            player_y_pos += speed_movement;
        }

        //Check box position
        if (player_y_pos < 0)
            player_y_pos = 0;

        if (player_y_pos > frame_height - player_size)
            player_y_pos = frame_height - player_size;

        player.setY(player_y_pos);
        score_label.setText("" + score);
    }


    public void animMenu() {

        //Speed of menu animation elements - calulate for different screen size
        in_anim_menu_enemy_1_x_pos -= screen_width / 300 + 1;
        in_anim_menu_enemy_2_x_pos -= screen_width / 300 + 2;
        in_anim_menu_enemy_3_x_pos -= screen_width / 300 + 4;

        if (in_anim_menu_enemy_1_x_pos < -img1.getWidth())
            in_anim_menu_enemy_1_x_pos = screen_width + player_size;

        img1.setX(in_anim_menu_enemy_1_x_pos);

        if (in_anim_menu_enemy_2_x_pos < -img2.getWidth())
            in_anim_menu_enemy_2_x_pos = screen_width + player_size;

        img2.setX(in_anim_menu_enemy_2_x_pos);

        if (in_anim_menu_enemy_3_x_pos < -img3.getWidth())
            in_anim_menu_enemy_3_x_pos = screen_width + player_size;

        img3.setX(in_anim_menu_enemy_3_x_pos);


    }


    public void gameOver(int score) {

        intent_result.putExtra("SCORE", score);
        startActivity(intent_result);

        enemy_cut.setVisibility(View.INVISIBLE);
        enemy_paper.setVisibility(View.INVISIBLE);
        enemy_rock.setVisibility(View.INVISIBLE);
        player.setVisibility(View.INVISIBLE);

        overridePendingTransition(0, 0);
        vibs.vibrate(100);
        //game_timer.cancel();
        //game_timer = null;
        finish();
        return;

    }


    public void hitCheck() {

        if (is_in_game) {

            //Enemy1
            int enemy1CenterX = enemy_1_x_pos + enemy1.getWidth() / 2;
            int enemy1CenterY = enemy_1_y_pos + enemy1.getHeight() / 2;

            //Enemy2
            int enemy2CenterX = enemy_2_x_pos + enemy2.getWidth() / 2;
            int enemy2CenterY = enemy_2_y_pos + enemy2.getHeight() / 2;

            //Enemy3
            int enemy3CenterX = enemy_3_x_pos + enemy3.getWidth() / 2;
            int enemy3CenterY = enemy_3_y_pos + enemy3.getHeight() / 2;

            //Enemy1
            if (0 <= enemy1CenterX &&
                    enemy1CenterX <= player_size &&
                    player_y_pos <= enemy1CenterY &&
                    enemy1CenterY <= player_y_pos + player_size) {

                if ((player_figure_id == enemy_1_figure_id) ||
                        (player_figure_id == 1 && enemy_1_figure_id == 3) ||
                        (player_figure_id == 2 && enemy_1_figure_id == 1) ||
                        (player_figure_id == 3 && enemy_1_figure_id == 2)) {

                    gameOver(score);

                } else {

                    player_figure_id = enemy_1_figure_id;

                    if (player_figure_id == 1)
                        player.setImageResource(R.drawable.cut_red);
                    if (player_figure_id == 2)
                        player.setImageResource(R.drawable.paper_blue);
                    if (player_figure_id == 3)
                        player.setImageResource(R.drawable.rock_green);

                    //new PlayCatchSoundOperationAsync().execute("");
                    catch_sound_player.start();
                    score += 10;
                    enemy_1_x_pos -= 1000;
                }
            }

            //Enemy2
            if (0 <= enemy2CenterX &&
                    enemy2CenterX <= player_size &&
                    player_y_pos <= enemy2CenterY &&
                    enemy2CenterY <= player_y_pos + player_size) {

                if ((player_figure_id == enemy_2_figure_id) ||
                        (player_figure_id == 1 && enemy_2_figure_id == 3) ||  //1 - nozyce
                        (player_figure_id == 2 && enemy_2_figure_id == 1) ||  //2 - papier
                        (player_figure_id == 3 && enemy_2_figure_id == 2)) {  //3 - kamien

                    gameOver(score);

                } else {

                    player_figure_id = enemy_2_figure_id;

                    if (player_figure_id == 1)
                        player.setImageResource(R.drawable.cut_red);
                    if (player_figure_id == 2)
                        player.setImageResource(R.drawable.paper_blue);
                    if (player_figure_id == 3)
                        player.setImageResource(R.drawable.rock_green);

                    //new PlayCatchSoundOperationAsync().execute("");
                    catch_sound_player.start();
                    score += 10;
                    enemy_2_x_pos -= 1000;
                }

            }

            //Enemy3
            if (0 <= enemy3CenterX &&
                    enemy3CenterX <= player_size &&
                    player_y_pos <= enemy3CenterY &&
                    enemy3CenterY <= player_y_pos + player_size) {

                if ((player_figure_id == enemy_3_figure_id) ||
                        (player_figure_id == 1 && enemy_3_figure_id == 3) ||
                        (player_figure_id == 2 && enemy_3_figure_id == 1) ||
                        (player_figure_id == 3 && enemy_3_figure_id == 2)) {

                    gameOver(score);

                } else {

                    player_figure_id = enemy_3_figure_id;

                    if (player_figure_id == 1)
                        player.setImageResource(R.drawable.cut_red);
                    if (player_figure_id == 2)
                        player.setImageResource(R.drawable.paper_blue);
                    if (player_figure_id == 3)
                        player.setImageResource(R.drawable.rock_green);

                    //new PlayCatchSoundOperationAsync().execute("");
                    catch_sound_player.start();
                    score += 10;
                    enemy_3_x_pos -= 1000;
                }
            }
        }
    }


    public boolean onTouchEvent(MotionEvent me) {

        if (!start_flag) {

            start_flag = true;

            final FrameLayout frame = (FrameLayout) findViewById(R.id.frame);

            frame_height = frame.getHeight();

            player_y_pos = (int) player.getY();
            player_size = player.getWidth();

            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);

            start_label_rock.setVisibility(View.INVISIBLE);
            start_label_paper.setVisibility(View.INVISIBLE);
            start_label_scissors.setVisibility(View.INVISIBLE);

            player.setVisibility(View.VISIBLE);
            score_label.setVisibility(View.VISIBLE);
            enemy1.setVisibility(View.VISIBLE);
            enemy2.setVisibility(View.VISIBLE);
            enemy3.setVisibility(View.VISIBLE);

            enemy_1_x_pos -= 1000;
            enemy_2_x_pos -= 1000;
            enemy_3_x_pos -= 1000;


            //Change position every 20 mili sec
            game_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    game_handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

            //Shake and color every 10 mili sec
            y_change_pos_and_color_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    y_change_pos_and_color_handler.post(new Runnable() {
                        @Override
                        public void run() {

                            direction = rm.nextInt(4) - 2;

                            //---------RED--------------

                            if (player_figure_id == 1) {

                                if (color) {
                                    i += 2;
                                } else {
                                    i -= 2;
                                }

                                if (i <= 0) {
                                    color = true;
                                }

                                if (i >= 80) {
                                    color = false;
                                }

                                if (j - 10 > 0)
                                    j -= 10;
                                if (k - 10 > 0)
                                    k -= 10;

                            }

                            //---------GREEN--------------

                            if (player_figure_id == 3) {
                                if (color) {
                                    j += 2;
                                } else {
                                    j -= 2;
                                }

                                if (j <= 0) {
                                    color = true;
                                }

                                if (j >= 80) {
                                    color = false;
                                }
                                if (i - 10 > 0)
                                    i -= 10;
                                if (k - 10 > 0)
                                    k -= 10;
                            }


                            //---------BLUE--------------

                            if (player_figure_id == 2) {
                                if (color) {
                                    k += 2;
                                } else {
                                    k -= 2;
                                }

                                if (k <= 0) {
                                    color = true;
                                }

                                if (k >= 80) {
                                    color = false;
                                }

                                if (i - 10 > 0)
                                    i -= 10;
                                if (j - 10 > 0)
                                    j -= 10;
                            }

                            if (score > 150) {
                                frame.setBackgroundColor(Color.rgb(i, j, k));
                                score_label.setBackgroundColor(Color.rgb(i, j, k));
                            }


                            if (direction < 0) {

                                if (score > 50) {

                                    enemy_1_y_pos -= screen_height / 1000;
                                    enemy_2_y_pos -= screen_height / 1000;
                                    enemy_3_y_pos -= screen_height / 1000;
                                }

                                if (score > 100) {

                                    enemy_1_y_pos -= screen_height / 500;
                                    enemy_2_y_pos -= screen_height / 500;
                                    enemy_3_y_pos -= screen_height / 500;
                                }
                                if (score > 200) {

                                    enemy_1_y_pos -= screen_height / 200;
                                    enemy_2_y_pos -= screen_height / 200;
                                    enemy_3_y_pos -= screen_height / 200;
                                }

                            } else {

                                if (score > 50) {

                                    enemy_1_y_pos += screen_height / 1000;
                                    enemy_2_y_pos += screen_height / 1000;
                                    enemy_3_y_pos += screen_height / 1000;
                                }

                                if (score > 100) {

                                    enemy_1_y_pos += screen_height / 500;
                                    enemy_2_y_pos += screen_height / 500;
                                    enemy_3_y_pos += screen_height / 500;
                                }
                                if (score > 200) {

                                    enemy_1_y_pos += screen_height / 200;
                                    enemy_2_y_pos += screen_height / 200;
                                    enemy_3_y_pos += screen_height / 200;
                                }

                            }

                        }
                    });

                }
            }, 0, 10);

        } else {

            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }

        }

        return true;
    }

}