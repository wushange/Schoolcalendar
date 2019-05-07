package com.wsg.schoolcalendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.wsg.schoolcalendar.bean.MyCalendar;
import com.wsg.schoolcalendar.push.EventBusCommon;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements
        CalendarView.OnViewChangeListener,
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
    private TextView tvCalendarDetail;
    private Button btnAdd;
    private Button btnDelete;


    private java.util.Calendar localCalendar;
    private int Year;       //年
    private int month;      //月
    private int day;        //日
    private int hour;       //时
    private int minute;     //分
    private int seconds;    //秒

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

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
        tvCalendarDetail = findViewById(R.id.tv_calendar_detail);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        initDateTime();

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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCaledar(mCalendarView.getSelectedCalendar());

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = mCalendarView.getSelectedCalendar();
                    MyCalendar myCalendar = x.getDb(MyApplication.getDaoConfig())
                            .selector(MyCalendar.class)
                            .where("year", "=", calendar.getYear())
                            .and("month", "=", calendar.getMonth())
                            .and("day", "=", calendar.getDay())
                            .findFirst();
                    if (myCalendar != null) {
                        x.getDb(MyApplication.getDaoConfig()).delete(myCalendar);
                        initData();
                        tvCalendarDetail.setText("");
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });


        initData();
    }

    private void initDateTime() {
        //实例化日期类
        localCalendar = java.util.Calendar.getInstance();
        Year = localCalendar.get(java.util.Calendar.YEAR);//获取当前年
        month = localCalendar.get(java.util.Calendar.MONTH) + 1;//获取月份，加1是因为月份是从0开始计算的
        day = localCalendar.get(java.util.Calendar.DATE);//获取日
        hour = localCalendar.get(java.util.Calendar.HOUR);//获取小时
        minute = localCalendar.get(java.util.Calendar.MINUTE);//获取分钟
        seconds = localCalendar.get(java.util.Calendar.SECOND);//获取秒钟
    }

    private void initData() {
        Map<String, Calendar> map = new HashMap<>();
        try {
            List<MyCalendar> myCalendars = x.getDb(MyApplication.getDaoConfig()).findAll(MyCalendar.class);
            if (myCalendars != null) {
                LogUtils.e("---" + myCalendars.size());
                if (myCalendars.size() > 0) {
                    for (int i = 0; i < myCalendars.size(); i++) {
                        Random random = new Random();
                        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                        MyCalendar myCalendar = myCalendars.get(i);
                        Calendar calendar = getSchemeCalendar(myCalendar.getYear(),
                                myCalendar.getMonth(),
                                myCalendar.getDay(),
                                ranColor,
                                myCalendar.getSchemeType(),
                                myCalendar.getScheme());
                        map.put(calendar.toString(),
                                calendar);
                        LogUtils.e("--" + myCalendars.get(i).toString());
                    }
                }

            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        btnDelete.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, int type, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        String s = "课";
        switch (type) {
            case 0:
                s = "课";
                break;
            case 1:
                s = "事";
                break;
            case 2:
                s = "议";
                break;
        }
        calendar.setScheme(s);
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        try {
            MyCalendar myCalendar = x.getDb(MyApplication.getDaoConfig())
                    .selector(MyCalendar.class)
                    .where("year", "=", calendar.getYear())
                    .and("month", "=", calendar.getMonth())
                    .and("day", "=", calendar.getDay())
                    .findFirst();
            mTvLunar.setVisibility(View.VISIBLE);
            mTvYear.setVisibility(View.VISIBLE);
            mTvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTvYear.setText(String.valueOf(calendar.getYear()));
            mTvLunar.setText(calendar.getLunar());
            mYear = calendar.getYear();

            if (myCalendar != null) {
                btnAdd.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                tvCalendarDetail.setText("无");
                btnAdd.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
            }
            if (isClick) {
                if (myCalendar != null) {
                    tvCalendarDetail.setText("类型：" + getType(myCalendar.getSchemeType()) + "\n\n内容：" + myCalendar.getScheme()
                            + "\n");
                } else {
                    tvCalendarDetail.setText("无");
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onYearChange(int year) {
        mTvMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarLongClick(final Calendar calendar) {
        addCaledar(calendar);
    }

    private void addCaledar(final Calendar calendar) {
        final MyCalendar myCalendar = new MyCalendar();
        myCalendar.setYear(calendar.getYear());
        myCalendar.setMonth(calendar.getMonth());
        myCalendar.setDay(calendar.getDay());

        MaterialDialog materialDialog = new MaterialDialog.Builder(context).title("添加事件")
                .customView(R.layout.custom_add_view, true)
                .positiveText("添加")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText etScheme = dialog.getCustomView().findViewById(R.id.et_scheme);
                        myCalendar.setScheme(etScheme.getText().toString());
                        try {
                            x.getDb(MyApplication.getDaoConfig())
                                    .save(myCalendar);
                            ToastUtils.showShort("保存成功");
                            initData();
                            initDateTime();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        initDateTime();
                        initData();
                    }
                })
                .build();
        View customview = materialDialog.getCustomView();
        Spinner spSchemeType = customview.findViewById(R.id.sp_scheme_type);
        CheckBox cbOpenCall = customview.findViewById(R.id.cb_open_call);
        final TextView tvCallTime = customview.findViewById(R.id.tv_call_time);
        tvCallTime.setText(TimeUtils.getNowString());
        tvCallTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化日期选择器悬浮窗
                //参数1：上下文对象
                //参数2：监听事件
                //参数3：初始化年份
                //参数4：初始化月份
                //参数5：初始化日期
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    //实现监听方法
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //设置文本显示内容，i为年，i1为月，i2为日
                        //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                        Year = i;
                        month = i1 + 1;
                        day = i2;
                        //实例化时间选择器
                        //参数1：上下文对象
                        //参数2：监听事件
                        //参数3：初始化小时
                        //参数4：初始化分钟
                        //参数5：是否24小时制
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            //实现监听方法
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                //设置文本显示内容
                                String timed = Year + "年" + month + "月" + day + "日   " + i + ":" + i1;
                                tvCallTime.setText(timed);
                                LogUtils.e("当前时间：" + Year + "年" + month + "月" + day + "日   " + i + ":" + i1);
                            }
                        }, hour, minute, true).show();//记得使用show才能显示
                    }
                }, Year, month - 1, day).show();//记得使用show才能显示悬浮窗
            }
        });
        final String[] spinnerItems = {"课程", "纪事", "会议"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSchemeType.setAdapter(spinnerAdapter);
        spSchemeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myCalendar.setSchemeType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        materialDialog.show();

    }

    public String getType(int type) {
        String string = "";
        switch (type) {
            case 0:
                string = "课程";
                break;
            case 1:
                string = "事件";
                break;
            case 2:
                string = "会议";
                break;
            default:
        }
        return string;
    }

    @Override
    public void onViewChange(boolean isMonthView) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refre(EventBusCommon a) {
        ToastUtils.showShort("开始查询最新日程");
    }
}
