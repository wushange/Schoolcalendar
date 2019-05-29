package com.wsg.schoolcalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.wsg.schoolcalendar.bean.Scheme;
import com.wsg.schoolcalendar.manager.AppManager;
import com.wsg.schoolcalendar.manager.ClockManager;
import com.wsg.schoolcalendar.push.EventBusCommon;
import com.wsg.schoolcalendar.receiver.ClockReceiver;
import com.wsg.schoolcalendar.service.ClockService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.qqtheme.framework.picker.DateTimePicker;


/**
 * 日历主界面
 */
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
	private RecyclerView tvCalendarDetail;
	private Button btnAdd;


	private java.util.Calendar localCalendar;
	private int lYear;       //年
	private int lMonth;      //月
	private int lDay;        //日
	private int lHour;       //时
	private int lMinute;     //分

	private DbManager dbManager;
	private SchemeAdapter schemeAdapter;
	private TextView tvWeek;

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
		BarUtils.setStatusBarLightMode(this, true);
		context = this;
		dbManager = AppManager.getInstance().getDBManager();

		//实例化控件


		tvWeek = findViewById(R.id.tv_week);

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
		tvCalendarDetail = findViewById(R.id.rcl_schemes);
		btnAdd = findViewById(R.id.btn_add);

		//对应日期的 日程课程会议等列表显示
		tvCalendarDetail.setLayoutManager(new LinearLayoutManager(getBaseContext()));
		schemeAdapter = new SchemeAdapter(new ArrayList<Scheme>());
		schemeAdapter.bindToRecyclerView(tvCalendarDetail);
		schemeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				ToastUtils.showShort("删除成功");
				AppManager.getInstance().deleteScheme(schemeAdapter.getItem(position));
				showSchemeDetails(mCalendarView.getSelectedCalendar());
				refresDatas();
			}
		});

		//设置日历长按监听
		mCalendarView.setOnCalendarLongClickListener(this);
		//设置日历点击选择监听
		mCalendarView.setOnCalendarSelectListener(this);
		//设置日历年份变化监听
		mCalendarView.setOnYearChangeListener(this);
		initDateTime();


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
		refresDatas();

	}

	private void initDateTime() {
		//实例化日期类
		localCalendar = java.util.Calendar.getInstance();
		lYear = localCalendar.get(java.util.Calendar.YEAR);//获取当前年
		lMonth = localCalendar.get(java.util.Calendar.MONTH) + 1;//获取月份，加1是因为月份是从0开始计算的
		lDay = localCalendar.get(java.util.Calendar.DATE);//获取日
		lHour = localCalendar.get(java.util.Calendar.HOUR);//获取小时
		lMinute = localCalendar.get(java.util.Calendar.MINUTE);//获取分钟
	}

	/**
	 * 初始化刷新日历数据
	 */
	private void refresDatas() {
		//从数据库获取是否显示日期标记
		Map<String, Calendar> map = AppManager.getInstance().getCalendarList();
		//此方法在巨大的数据量上不影响遍历性能，推荐使用
		mCalendarView.setSchemeDate(map);
		showSchemeDetails(mCalendarView.getSelectedCalendar());

	}

	@Override
	public void onCalendarOutOfRange(Calendar calendar) {

	}


	@SuppressLint("SetTextI18n")
	@Override
	public void onCalendarSelect(Calendar calendar, boolean isClick) {
		showSchemeDetails(calendar);
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

	/**
	 * 添加一个日程
	 *
	 * @param calendar 当前选择的日期
	 */
	private void addCaledar(final Calendar calendar) {
		final Scheme scheme = new Scheme();
		scheme.setYear(calendar.getYear());
		scheme.setMonth(calendar.getMonth());
		scheme.setDay(calendar.getDay());
		scheme.setSchemetime(scheme.getYear() + "-" + scheme.getMonth() + "-" + scheme.getDay());

		MaterialDialog materialDialog = new MaterialDialog.Builder(context).title("添加事件")
				.customView(R.layout.custom_add_view, true)
				.positiveText("添加")
				.negativeText("取消")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						EditText etScheme = dialog.getCustomView().findViewById(R.id.et_scheme);
						scheme.setScheme(etScheme.getText().toString());
						AppManager.getInstance().addScheme(scheme);
						ToastUtils.showShort("保存成功");
						refresDatas();
						initDateTime();
						Calendar calendar1 = mCalendarView.getSelectedCalendar();
						showSchemeDetails(calendar1);
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
						initDateTime();
						refresDatas();
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
				DateTimePicker dateTimePicker = new DateTimePicker((Activity) context, DateTimePicker.HOUR_24);
				dateTimePicker.setDateRangeStart(1976, 1, 1);
				dateTimePicker.setDateRangeEnd(2099, 12, 31);
				dateTimePicker.setSelectedItem(lYear, lMonth, lDay, lHour, lMinute);
				dateTimePicker.setOnWheelListener(new DateTimePicker.OnWheelListener() {
					@Override
					public void onYearWheeled(int index, String year) {
						lYear = Integer.parseInt(year);
					}

					@Override
					public void onMonthWheeled(int index, String month) {
						lMonth = Integer.parseInt(month);
					}

					@Override
					public void onDayWheeled(int index, String day) {
						lDay = Integer.parseInt(day);
					}

					@Override
					public void onHourWheeled(int index, String hour) {
						lHour = Integer.parseInt(hour);
					}

					@Override
					public void onMinuteWheeled(int index, String minute) {
						lMinute = Integer.parseInt(minute);
					}
				});
				dateTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						tvCallTime.setText(lYear + "-" + lMonth + "-" + lDay + " " + lHour + ":" + lMinute);
						scheme.setSchemetime(lYear + "-" + lMonth + "-" + lDay + " " + lHour + ":" + lMinute);
					}
				});
				dateTimePicker.show();
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
				scheme.setSchemetype(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		materialDialog.show();

	}

	/**
	 * 显示当前日期的日程详情列表
	 *
	 * @param calendar 当前日期
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void showSchemeDetails(Calendar calendar) {
		try {
			//从数据库读取 当前 Calendar 下的日程列表数据
			List<Scheme> myCalendar = dbManager
					.selector(Scheme.class)
					.where("year", "=", calendar.getYear())
					.and("month", "=", calendar.getMonth())
					.and("day", "=", calendar.getDay())
					.findAll();
			mTvLunar.setVisibility(View.VISIBLE);
			mTvYear.setVisibility(View.VISIBLE);
			mTvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
			mTvYear.setText(String.valueOf(calendar.getYear()));
			mTvLunar.setText(calendar.getLunar());

			java.util.Calendar cal = java.util.Calendar.getInstance();//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
			cal.setFirstDayOfWeek(java.util.Calendar.MONDAY); // 设置每周的第一天为星期一
			cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);// 每周从周一开始
			cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天

			Date date = null;
			try {
				//设置起始周
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				date = sdf.parse("2019-02-25");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long weekMil = (60 * 60 * 60 * 24 * 7) * 1000;
			long longDate = date.getTime() - weekMil;
			cal.setTime(new Date(calendar.getTimeInMillis() - longDate));
			int weeks = cal.get(java.util.Calendar.WEEK_OF_YEAR);
			tvWeek.setText("第" + weeks + "周");
			//为Recyclerview设置数据源
			if (myCalendar != null) {
				schemeAdapter.setNewData(myCalendar);
			} else {
				schemeAdapter.setNewData(null);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onViewChange(boolean isMonthView) {

	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void refre(EventBusCommon a) {
		ToastUtils.showShort("更新了一个Scheme！");
		refresDatas();
		initDateTime();
	}
}
