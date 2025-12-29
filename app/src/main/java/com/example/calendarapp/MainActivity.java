package com.example.calendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private GridView calendarGrid;
    private ArrayList<Integer> dayList;
    private Calendar calendar;
    private int displayYear;
    private int displayMonth;
    private TextView textYear;
    private TextView textMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //　カレンダーの枠を取得
        calendarGrid = findViewById(R.id.calendarGrid);

        // 今日の年月日時を取得
        calendar = Calendar.getInstance();
        // 取得した年を取り出す
        displayYear = calendar.get(Calendar.YEAR);
        // 取得した月を取り出す
        displayMonth = calendar.get(Calendar.MONTH) + 1;

        // カレンダーの年月表示個所を取得
        textYear = findViewById(R.id.textYear);
        textMonth = findViewById(R.id.textMonth);
        // 画面に年月を表示
        textYear.setText(displayYear + "年");
        textMonth.setText(displayMonth + "月");

        // カレンダーのボタンを取得
        Button btnPrevYear = findViewById(R.id.btnPrevYear);
        Button btnNextYear = findViewById(R.id.btnNextYear);
        Button btnPrevMonth = findViewById(R.id.btnPrevMonth);
        Button btnNextMonth = findViewById(R.id.btnNextMonth);
        // カレンダーのボタンのリスナーを設定
        btnPrevYear.setOnClickListener(v -> changeYear(-1));
        btnNextYear.setOnClickListener(v -> changeYear(1));
        btnPrevMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(1));

        // 日付リストを作成
        dayList = createDayList();

        // Adapterに日付リストを渡す
        CalendarAdapter adapter = new CalendarAdapter(this, dayList, displayYear, displayMonth);
        // カレンダーの枠にアダプターを設定
        calendarGrid.setAdapter(adapter);

        // セルをタップしたときの処理
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            // 日付を取得
            int displayDay = dayList.get(position);

            // 空白マス (=0) は無視
            if (displayDay == 0) return;

            // AddEventActivityへ画面遷移
            Intent intent = new Intent(this, AddEventActivity.class);
            // 新しい画面に年月日を渡す
            intent.putExtra("year", displayYear);
            intent.putExtra("month", displayMonth);
            intent.putExtra("day", displayDay);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 予定を書き戻した時に更新する
        ((BaseAdapter) calendarGrid.getAdapter()).notifyDataSetChanged();
    }


    // 年変更用メソッド
    private void changeYear(int offset) {
        displayYear += offset;
        updateCalendar();
    }

    // 月変更用メソッド
    private void changeMonth(int offset) {
        displayMonth += offset;
        if (displayMonth < 1) {
            displayMonth = 12;
            displayYear--;
        } else if (displayMonth > 12) {
            displayMonth = 1;
            displayYear++;
        }
        updateCalendar();
    }

    // カレンダーの表示を更新する
    private void updateCalendar() {
        textYear.setText(displayYear + "年");
        textMonth.setText(displayMonth + "月");
        dayList = createDayList();
        CalendarAdapter adapter = new CalendarAdapter(this, dayList, displayYear, displayMonth);
        calendarGrid.setAdapter(adapter);
    }

    // 日付リストの作成
    private ArrayList<Integer> createDayList() {
        ArrayList<Integer> list = new ArrayList<>();

        // カレンダーに今月の1日（月初）に設定
        calendar.set(displayYear, displayMonth, 1);

        // 月初の曜日を取得
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //　今月の最大値（日）を取得
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 空白（前月分）
        // 月初の曜日以前の場合は、Listに0を追加
        for (int i = 1; i < startDayOfWeek; i++) {
            list.add(0);
        }

        // 今月末までの日付をListに追加
        for (int day = 1; day <= maxDay; day++) {
            list.add(day);
        }

        return list;
    }
}