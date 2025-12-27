package com.example.calendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private TextView btnSave;
    private TextView txtDate;
    private TextView txtTime;
    private TextView editEvent;
    private int inputYear;
    private int inputMonth;
    private int inputDay;
    private int inputHour;
    private int inputMinute;

    private int initialYear;
    private int initialMonth;
    private int initialDay;
    private int initialHour;
    private int initialMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 予定入力欄を取得
        editEvent = findViewById(R.id.editEvent);
        // 年月日入力欄を取得
        txtDate = findViewById(R.id.txtDate);
        // 時間入力欄を取得
        txtTime = findViewById(R.id.txtTime);
        // 予定入力欄を取得
        editEvent = findViewById(R.id.editEvent);
        // 保存ボタンを取得
        btnSave = findViewById(R.id.btnSave);

        // 選択したセルの年月日を取得
        Intent intent = getIntent();
        initialYear = intent.getIntExtra("year", 0);
        initialMonth = intent.getIntExtra("month", 0);
        initialDay = intent.getIntExtra("day", 0);
        // 現在時刻を取得
        Calendar calendar = Calendar.getInstance();
        initialHour = calendar.get(Calendar.HOUR_OF_DAY);
        initialMinute = calendar.get(Calendar.MINUTE);
        // 初期値として年月日入力欄に選択したセルの年月日を表示
        txtDate.setText(initialYear + "/" + initialMonth + "/" + initialDay);
        // 初期値として時刻表示欄に現在時刻を表示
        txtTime.setText(String.format("%02d:%02d", initialHour, initialMinute));

        // 入力値として初期値を代入（何も触らずに保存したとき用）
        inputYear = initialYear;
        inputMonth = initialMonth;
        inputDay = initialDay;
        inputHour = initialHour;
        inputMinute = initialMinute;

        // 入力した年月日を取得
        txtDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    AddEventActivity.this,
                    (view, y, m, d) -> {
                        inputYear = y;
                        inputMonth = m + 1;
                        inputDay = d;
                        // 画面に反映
                        txtDate.setText(y + "/" + (m + 1) + "/" + d);
                    },
                    // ピッカーの初期値
                    initialYear, initialMonth - 1, initialDay
            );
            dialog.show();
        });

        // 入力した時間を取得
        txtTime.setOnClickListener(v -> {

            TimePickerDialog dialog = new TimePickerDialog(
                    AddEventActivity.this,
                    (view, h, m) -> {
                        inputHour = h;
                        inputMinute = m;
                        // 画面に反映
                        txtTime.setText(String.format("%02d:%02d", h, m));
                    },
                    // ピッカーの初期値
                    initialHour, initialMinute, true
            );
            dialog.show();
        });

        // 保存ボタンをクリックしたときの処理
        btnSave.setOnClickListener(v -> {
            // 入力した予定を取得
            String inputEvent = editEvent.getText().toString();
            saveEvent(inputYear, inputMonth, inputDay, inputHour, inputMinute, inputEvent);
            finish();
        });
    }

    // 保存ボタンを押したときの処理
    private void saveEvent(int year, int month, int day, int hour, int minute, String event) {
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        String key = year + "-" + month + "-" + day;

        // 既存データ
        String old = prefs.getString(key, "");
        // 記録フォーマット
        String one = String.format("%02d:%02d", hour, minute) + " - " + event;

        String updated;
        if (old.isEmpty()) {
            updated = one;
        } else {
            updated = old + "\n" + one;
        }

        prefs.edit().putString(key, updated).apply();

        // ★ログ出力で確認
        Log.d("SAVE_EVENT", "Key = " + key);
        Log.d("SAVE_EVENT", "Saved = " + updated);
    }
}