package com.labs.lab1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    GridView mainBack;
    CustomAdapter customAdapter;
    ImageView pause;
    int cols, rows;
    boolean mTimerRun;
    TextView mRecord;
    TextView mTimer;
    int timer,recordTry;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_ROWS = "rows";
    public static final String APP_PREFERENCES_RECORD = "record";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_MULTI_PROCESS);
        recordTry = mSettings.getInt(APP_PREFERENCES_RECORD,999999999);
        mRecord = (TextView) findViewById(R.id.record);
        if (recordTry != 999999999) {
            int minutes = recordTry / 3600;
            int seconds = (recordTry % 3600) / 60;
            int milisec = recordTry % 60;

            String textTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milisec);
            mRecord.setText(textTime);
        }
        runChrono();
        rows = mSettings.getInt(APP_PREFERENCES_ROWS,4);
        cols = 4;
        pause = (ImageView) findViewById(R.id.btnPause);
        mainBack = (GridView) findViewById(R.id.background);
        mainBack.setNumColumns(cols);
        mainBack.setEnabled(true);
        customAdapter = new CustomAdapter(this,cols,rows);
        mainBack.setAdapter(customAdapter);
        mainBack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                customAdapter.checkOpenCards();
                customAdapter.openCard(position);
                if (customAdapter.checkGameOver()) {
                    mTimerRun = false;
                    if (timer<recordTry) {
                        recordTry = timer;
                        int minutes = recordTry/3600;
                        int seconds = (recordTry%3600)/60;
                        int milisec = recordTry%60;

                        String textTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",minutes,seconds,milisec);
                        mRecord.setText(textTime);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putInt(APP_PREFERENCES_RECORD, recordTry);
                        editor.apply();
                        Toast.makeText(GameActivity.this, "New Record!", Toast.LENGTH_SHORT).show();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customdialog2, viewGroup, false);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                    final ImageView contin = (ImageView) dialogView.findViewById(R.id.btnOk);
                    ImageView cancel = (ImageView) dialogView.findViewById(R.id.btnCancel);
                    contin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customAdapter = new CustomAdapter(getApplicationContext(),cols,rows);
                            mainBack.setAdapter(customAdapter);
                            alertDialog.cancel();
                            onClickRestart(contin);
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPause(pause);
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customdialog, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                ImageView play = (ImageView) dialogView.findViewById(R.id.btnPlay);
                final ImageView restart = (ImageView) dialogView.findViewById(R.id.btnRestart);
                ImageView menu = (ImageView) dialogView.findViewById(R.id.btnMenu);
                restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customAdapter = new CustomAdapter(getApplicationContext(),cols,rows);
                        mainBack.setAdapter(customAdapter);
                        alertDialog.cancel();
                        onClickRestart(restart);
                    }
                });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTimerRun = true;
                        alertDialog.cancel();
                    }
                });
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                if (!alertDialog.isShowing()) {
                    mTimerRun = true;
                }
            }
        });
    }

    private void onClickRestart(View view) {
        timer = 0;
        mTimerRun = true;
    }

    private void onClickPause(View view) {
    mTimerRun = false;
    }

    private void runChrono() {
        mTimer = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
            int minutes = timer/3600;
            int seconds = (timer%3600)/60;
            int milisec = timer%60;

            String textTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",minutes,seconds,milisec);
            mTimer.setText(textTime);
            if (mTimerRun) {
                timer++;
            }
            handler.postDelayed(this,1);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mTimerRun = hasFocus;
        super.onWindowFocusChanged(hasFocus);
    }


}
