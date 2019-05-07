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
import com.wsg.schoolcalendar.bean.User;

import org.xutils.ex.DbException;
import org.xutils.x;

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

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =mEtAccount.getText().toString().trim() ;
                String pwd = mEtPassword.getText().toString().trim();
                if (name.isEmpty()) {
                    ToastUtils.showShort("账号不能为空！");
                    return;
                }
                if (pwd.isEmpty()) {
                    ToastUtils.showShort("账号不能为空！");
                    return;
                }

                try {
                    User user = x.getDb(MyApplication.getDaoConfig()).selector(User.class)
                            .where("name","=",name)
                            .and("password","=",pwd)
                            .findFirst();
                    if(user!=null){
                        ActivityUtils.startActivity(MainActivity.class);
                    }else{
                        ToastUtils.showShort("用户不存在！或者密码错误");
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
