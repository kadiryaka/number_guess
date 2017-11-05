package com.kadiryaka.numberguess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.*;


public class GameActivity extends ActionBarActivity {

    private SeekBar seekBar;
    private TextView textView;
    private Button tryButton;
    private TextView leftTry;
    private Button newGame;
    private ImageView volume;
    private ImageView highScore;
    private Button newGameBottom;
    private Integer counter;
    private Boolean volControl;
    private int minValue;
    private int maxValue;
    private int i;
    MediaPlayer mp = null;
    private Boolean arrowControl;
    Toast toast;
    private SharedPreferences prefs;
    private int oldScore;
    private int oldScore1;
    private int oldScore2;
    private int oldScore3;
    private int tryCount;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //üst başlığı kaldırmak için
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int width = displaymetrics.widthPixels/2;
        final Context context = this;

        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        //HeadActivity den gelen verileri alıyoruz
        Bundle bundle = new  Bundle();
        bundle = getIntent().getExtras();
        if (bundle.getString("index").equals("0")) {
            maxValue = 250;
            oldScore = prefs.getInt("key0", 6);
            key = "key0";
            tryCount = 6;
            setHighScore(prefs);
        } else if (bundle.getString("index").equals("1")) {
            maxValue = 5000;
            oldScore = prefs.getInt("key1", 11);
            key = "key1";
            tryCount = 11;
            setHighScore(prefs);
        } else if (bundle.getString("index").equals("2")) {
            maxValue = 1000000;
            oldScore = prefs.getInt("key2", 17);
            key = "key2";
            tryCount = 17;
            setHighScore(prefs);
        } else {
            maxValue = 100;
            oldScore = prefs.getInt("key3", 3);
            key = "key3";
            tryCount = 3;
            setHighScore(prefs);
        }

        final Random r = new Random();
        final Integer randNumber = r.nextInt(maxValue);
        counter = 0;
        volControl = true;
        minValue = 0;
        arrowControl = false;

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setMax(maxValue);
        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
        textView = (TextView) findViewById(R.id.textView1);
        leftTry = (TextView) findViewById(R.id.leftTry);
        tryButton = (Button) findViewById(R.id.tryBut);
        newGame = (Button) findViewById(R.id.newGame);
        volume = (ImageView) findViewById(R.id.volume);
        highScore = (ImageView) findViewById(R.id.bestScore);
        newGameBottom = (Button) findViewById(R.id.newGameBottom);
        textView.setText(minValue + " < " +seekBar.getProgress() + " < " + maxValue);
        leftTry.setText(getString(R.string.leftTry) + counter);


        //başlangıçtaki timer
        final CountDownTimer countDownExample = new CountDownExample(5000, 60);
        countDownExample.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                countDownExample.cancel();
            }
        }, 2000);


        toast = Toast.makeText(this, R.string.useBar, Toast.LENGTH_LONG);
        toast.setText("" + getString(R.string.useBar));
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

        //seekBar değiştirildiği zaman olacaklar
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                        int progresValue, boolean fromUser) {
                        seekBar.setProgress(progresValue);
                        textView.setText(minValue + " < " +(seekBar.getProgress()+minValue) + " < " + maxValue);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                    }
                });

        //tek tek artırma azalma click olayları
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                arrowControl = true;
                if (event.getRawX()< width) {
                    seekBar.setProgress(seekBar.getProgress() - 1);

                } else {
                    seekBar.setProgress(seekBar.getProgress() + 1);
                }
                textView.setText(minValue + " < " + (seekBar.getProgress() + minValue) + " < " + maxValue);
                return false;
            }
        });

        //alttaki new game butonu
        newGameBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGameScreen();
            }
        });

        //yüksek skor mevzusu
        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(
                        getString(R.string.highScores) + "\n" +
                        getString(R.string.value1) + " : " + oldScore1 +
                        "\n" + getString(R.string.value2) + " : " + oldScore2 +
                        "\n" + getString(R.string.value3) + " : " + oldScore3)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //dene butonuna basılınca
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volControl) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.game_false);
                    mp.start();
                }
                counter++;
                if (counter != tryCount) {
                    if ((seekBar.getProgress() + minValue) == randNumber) {
                        //success
                        if (volControl) {
                            mp = MediaPlayer.create(getApplicationContext(), R.raw.game_true);
                            mp.start();
                        }
                        if (counter < oldScore) {
                            //yeni yüksek skor varsa önce bu rakam commit ediliyor
                            //sonra ekrana mesaj gösteriliyor
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putInt(key, (counter));
                            edit.commit();
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage(getString(R.string.newHighScore) + counter)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                        leftTry.setText(getString(R.string.leftTry) + counter);
                        newGame.setText(R.string.result);
                        newGame.setVisibility(View.VISIBLE);
                        tryButton.setVisibility(View.INVISIBLE);

                    } else {
                        //continue
                        if ((seekBar.getProgress() + minValue) > randNumber) {
                            maxValue = (seekBar.getProgress() + minValue);
                            seekBar.setMax(maxValue - minValue);
                            textView.setText(minValue + " < " + maxValue + " < " + maxValue);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setText(getString(R.string.down));
                            toast.show();
                            if (counter == 5 && !arrowControl) {
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setText(getString(R.string.arrowWarning));
                                toast.show();
                            }
                            leftTry.setText(getString(R.string.leftTry) + counter);
                        } else {
                            minValue = (seekBar.getProgress() + minValue);
                            seekBar.setMax(maxValue - minValue);
                            textView.setText(minValue + " < " + minValue + " < " + maxValue);
                            leftTry.setText(getString(R.string.leftTry) + counter);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setText(getString(R.string.up));
                            toast.show();
                            if (counter == 2 && !arrowControl) {
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setText(getString(R.string.arrowWarning));
                                toast.show();
                            }
                        }
                    }
                } else {
                    leftTry.setText(getString(R.string.notFound) + " " + randNumber);
                    tryButton.setVisibility(View.INVISIBLE);
                    newGame.setText(R.string.lost);
                    newGame.setVisibility(View.VISIBLE);

                }

            }
        });

        //oyun sonu new game butonu
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity is recreating
                newGameScreen();
            }
        });

        //ses açma kapama
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVolumeBool(volControl);
            }
        });

    }

    public class CountDownExample extends CountDownTimer{

        public CountDownExample(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            seekBar.setProgress(((int) millisUntilFinished) / 50);
            System.out.println(millisUntilFinished);
        }

        @Override
        public void onFinish() {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeVolumeBool(Boolean volBool) {
        volControl = !volBool;
        if (volControl)
            volume.setImageResource(R.drawable.vol2);
        else
            volume.setImageResource(R.drawable.vol);
    }

    public void newGameScreen() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void setHighScore(SharedPreferences prefs) {
        oldScore1 = prefs.getInt("key0", 6);
        oldScore2 = prefs.getInt("key1", 11);
        oldScore3 = prefs.getInt("key2", 17);
    }

}
