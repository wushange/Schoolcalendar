package com.wsg.schoolcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText mEtAccount;
    private TextInputEditText mEtPassword;
    private Button mBtnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BarUtils.setStatusBarLightMode(this,true);
        mEtAccount = findViewById(R.id.et_account);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startActivity(MainActivity.class);
            }
        });

    }
}
