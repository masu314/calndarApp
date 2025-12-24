package com.example.calendarapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> dayList;
    private LayoutInflater inflater;

    private int todayYear;
    private int todayMonth;
    private int todayDay;


    // コンストラクタ
    public CalendarAdapter(Context context, ArrayList<Integer> dayList) {
        this.context = context;
        this.dayList = dayList;
        this.inflater = LayoutInflater.from(context);

        // 今日の日時を取得
        Calendar today = Calendar.getInstance();
        // 今年の年を取得
        todayYear = today.get(Calendar.YEAR);
        // 今月の月を取得
        todayMonth = today.get(Calendar.MONTH);
        // 今日の日付を取得
        todayDay = today.get(Calendar.DAY_OF_MONTH);
    }

    // カレンダーのマスの数を取得
    @Override
    public int getCount() {
        return dayList.size();
    }

    // 特定の位置にある日付データを取得
    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // カレンダー1マスの見た目を設定
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // viewの使いまわし
        View view = convertView;
        // view が空の場合は、xmlから新たに作成
        if (view == null) {
            view = inflater.inflate(R.layout.calendar_cell, parent, false);
        }

        // 日付表示用のTextViewを取得
        TextView dayText = view.findViewById(R.id.dayText);
        // positionから日付を取り出す
        int day = dayList.get(position);

        // 空白マスの処理
        if (day == 0) {
            // 日付を表示しない
            dayText.setText("");
            // 背景を設定しない
            view.setBackgroundColor(Color.TRANSPARENT);
        // 通常の日付マスの処理
        } else {
            // 日付を表示
            dayText.setText(String.valueOf(day));
            dayText.setTextColor(Color.BLACK);

            // 今日かどうかを判定
            if (isToday(day)) {
                view.setBackgroundColor(Color.parseColor("#FFCDD2")); // 薄い赤
            } else {
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        return view;
    }

    // 今日かどうかを判定する処理
    private boolean isToday(int day) {
        // 今日の日時を取得
        Calendar cal = Calendar.getInstance();
        // 今年の年と今月の月と今日の日付と一致していたら true を返す
        return cal.get(Calendar.YEAR) == todayYear
                && cal.get(Calendar.MONTH) == todayMonth
                && day == todayDay;
    }
}