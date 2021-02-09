package com.labs.lab1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_ROWS = "rows";
    private int rows;
    private String strRows;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_MULTI_PROCESS);
        rows = mSettings.getInt(APP_PREFERENCES_ROWS,4);
        final TextView numRows = (TextView) findViewById(R.id.textRows);
        TextView upRows = (TextView) findViewById(R.id.upRows);
        TextView downRows = (TextView) findViewById(R.id.downRows);
        strRows = Integer.toString(rows);
        numRows.setText(strRows);
        upRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rows < 7)
                rows++;
                strRows = Integer.toString(rows);
                numRows.setText(strRows);
            }
        });
        downRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rows > 1)
                    rows--;
                strRows = Integer.toString(rows);
                numRows.setText(strRows);
            }
        });
        ImageView back = (ImageView) findViewById(R.id.btnBackSet);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_ROWS, rows);
        editor.apply();
    }
}
