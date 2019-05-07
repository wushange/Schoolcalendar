package com.wsg.schoolcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wsg.schoolcalendar.bean.Result;
import com.wsg.schoolcalendar.bean.User;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {
    private TextInputEditText mEtAccount;
    private TextInputEditText mEtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BarUtils.setStatusBarLightMode(this, true);
        mEtAccount = findViewById(R.id.et_account);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);

        //点击登陆按钮
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startActivity(MainActivity.class);
                //检查输入情况账号密码
                String name = mEtAccount.getText().toString().trim();
                String pwd = mEtPassword.getText().toString().trim();
                if (name.isEmpty()) {
                    ToastUtils.showShort("账号不能为空！");
                    return;
                }
                if (pwd.isEmpty()) {
                    ToastUtils.showShort("账号不能为空！");
                    return;
                }

                //请求网络，
                RequestParams requestParams = new RequestParams();
                requestParams.addBodyParameter("name", name);
                requestParams.addBodyParameter("pws", pwd);

                x.http().post(requestParams, new Callback.CommonCallback<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        //返回结果判断是否登陆成功
                        if (result.getCode() == 0) {
                            ToastUtils.showShort("登陆成功！");
                            ActivityUtils.startActivity(MainActivity.class);
                        } else {
                            ToastUtils.showShort("用户不存在！或者密码错误");
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
//                try {
//                    User user = x.getDb(MyApplication.getDaoConfig()).selector(User.class)
//                            .where("name", "=", name)
//                            .and("password", "=", pwd)
//                            .findFirst();
//                    if (user != null) {
//                        ActivityUtils.startActivity(MainActivity.class);
//                    } else {
//                        ToastUtils.showShort("用户不存在！或者密码错误");
//                    }
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }

            }
        });

    }
}
