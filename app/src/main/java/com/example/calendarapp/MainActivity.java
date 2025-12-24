package com.example.calendarapp;

import android.os.Bundle;
import android.widget.GridView;

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

        // 日付リストを作成
        dayList = createDayList();

        // Adapterに日付リストを渡す
        CalendarAdapter adapter = new CalendarAdapter(this, dayList);
        // カレンダーの枠にアダプターを設定
        calendarGrid.setAdapter(adapter);
    }


    // 日付リストの作成
    private ArrayList<Integer> createDayList() {
        ArrayList<Integer> list = new ArrayList<>();

        // 今日の日時を取得
        Calendar calendar = Calendar.getInstance();

        // 今年を取得
        int year = calendar.get(Calendar.YEAR);
        // 今月を取得
        int month = calendar.get(Calendar.MONTH);

        // カレンダーに今月の1日（月初）に設定
        calendar.set(year, month, 1);

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