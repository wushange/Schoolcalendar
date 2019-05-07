package com.wsg.schoolcalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.wsg.schoolcalendar.bean.MyCalendar;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener, CalendarView.OnCalendarLongClickListener {

    private Context context;
    private RelativeLayout mRlTool;
    private TextView mTvMonthDay;
    private TextView mTvYear;
    private TextView mTvLunar;
    private FrameLayout mFlCurrent;
    private ImageView mIbCalendar;
    private TextView mTvCurrentDay;
    private CalendarLayout mCalendarLayout;
    private CalendarView mCalendarView;
    private NestedScrollView mNestedScrollView;
    private LinearLayout mLinearView;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        BarUtils.setStatusBarLightMode(this, true);

        mRlTool = findViewById(R.id.rl_tool);
        mTvMonthDay = findViewById(R.id.tv_month_day);
        mTvYear = findViewById(R.id.tv_year);
        mTvLunar = findViewById(R.id.tv_lunar);
        mFlCurrent = findViewById(R.id.fl_current);
        mIbCalendar = findViewById(R.id.ib_calendar);
        mTvCurrentDay = findViewById(R.id.tv_current_day);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView = findViewById(R.id.calendarView);
        mNestedScrollView = findViewById(R.id.nestedScrollView);
        mLinearView = findViewById(R.id.linearView);

        mCalendarView.setOnCalendarLongClickListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mYear = mCalendarView.getCurYear();
        mTvMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTvLunar.setText("今日");
        mTvCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        mFlCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarView.scrollToCurrent();
            }
        });
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        try {
            List<MyCalendar> myCalendars = x.getDb(MyApplication.getDaoConfig()).findAll(MyCalendar.class);
            if(myCalendars!=null){
            LogUtils.e("---"+ myCalendars.size());
            if(myCalendars.size()>0){
                for (int i = 0; i < myCalendars.size(); i++) {
                    LogUtils.e("--"+ myCalendars.get(i).toString());
                }
            }}
        } catch (DbException e) {
            e.printStackTrace();
        }

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 22, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 22, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));

        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(color, "假");
        calendar.addScheme(day % 2 == 0 ? 0xFF00CD00 : 0xFFD15FEE, "节");
        calendar.addScheme(day % 2 == 0 ? 0xFF660000 : 0xFF4169E1, "记");
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTvLunar.setVisibility(View.VISIBLE);
        mTvYear.setVisibility(View.VISIBLE);
        mTvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTvYear.setText(String.valueOf(calendar.getYear()));
        mTvLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {

        }
    }

    @Override
    public void onYearChange(int year) {
        mTvMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }
      String yy = "";
    @Override
    public void onCalendarLongClick(final Calendar calendar) {

        new MaterialDialog.Builder(context)
                .title("添加事件")
                .autoDismiss(false)
                .input("输入事件内容", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    }
                })
                .positiveText("添加")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        MyCalendar myCalendar = new MyCalendar();
                        myCalendar.setYear(calendar.getYear());
                        myCalendar.setMonth(calendar.getMonth());
                        myCalendar.setDay(calendar.getDay());
                        myCalendar.setScheme(dialog.getInputEditText().getText().toString());
                        try {
                            x.getDb(MyApplication.getDaoConfig())
                                    .save(myCalendar);
                            ToastUtils.showShort("保存成功");
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();

    }
}
