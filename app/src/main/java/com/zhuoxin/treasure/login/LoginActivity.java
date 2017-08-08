package com.zhuoxin.treasure.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuoxin.treasure.MainActivity;
import com.zhuoxin.treasure.R;
import com.zhuoxin.treasure.User;
import com.zhuoxin.treasure.commons.ActivityUtils;
import com.zhuoxin.treasure.commons.RegexUtils;
import com.zhuoxin.treasure.custom.AlertDialogFragment;
import com.zhuoxin.treasure.map.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.btn_Login)
    Button btnLogin;
    private Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.login);
        }
        etPassword.addTextChangedListener(mTextWatcher);
        etUsername.addTextChangedListener(mTextWatcher);
    }

    private String passWord;
    private String uaerName;
    private TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            passWord = etPassword.getText().toString();
            uaerName = etUsername.getText().toString();

            boolean canLogin=!TextUtils.isEmpty(passWord)&&!TextUtils.isEmpty(uaerName);
            btnLogin.setEnabled(canLogin);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_forgetPassword, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPassword:
                mActivityUtils.showToast("忘记密码");
                break;
            case R.id.btn_Login:
                if (RegexUtils.verifyUsername(uaerName)!=RegexUtils.VERIFY_SUCCESS){
                    AlertDialogFragment.getinstance(getString(R.string.username_error),getString(R.string.username_rules))
                    .show(getSupportFragmentManager(),"username_error");
                    return;
                }
                if (RegexUtils.verifyPassword(passWord)!=RegexUtils.VERIFY_SUCCESS){
                    AlertDialogFragment.getinstance(getString(R.string.password_error),getString(R.string.password_rules))
                            .show(getSupportFragmentManager(),"password_error");
                    return;
                }
                new LoginPresenter(this).login(new User(uaerName,passWord));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "登录", "正在登陆中，请稍候......");

    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void navigateToHomeActivity() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();

        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));
    }
}
