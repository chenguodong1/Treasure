package com.zhuoxin.treasure;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhuoxin.treasure.commons.ActivityUtils;
import com.zhuoxin.treasure.login.LoginActivity;
import com.zhuoxin.treasure.map.HomeActivity;
import com.zhuoxin.treasure.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.btn_Login)
    Button btnLogin;
    private Unbinder mUnbind;
    private ActivityUtils mActivityUtils;
    public static final String ACTION_MAIN="action_main";
    BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
        if (UserPrefs.getInstance().getTokenid()==user_info.getInt("key_tokenid",0)){
            mActivityUtils.startActivity(HomeActivity.class);
            finish();
            return;
        }
        IntentFilter intentFilter=new IntentFilter(ACTION_MAIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();;
    }
}
