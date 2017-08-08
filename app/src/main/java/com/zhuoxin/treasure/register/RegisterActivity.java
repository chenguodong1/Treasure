package com.zhuoxin.treasure.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.treasure.MainActivity;
import com.zhuoxin.treasure.R;
import com.zhuoxin.treasure.User;
import com.zhuoxin.treasure.commons.ActivityUtils;
import com.zhuoxin.treasure.commons.RegexUtils;
import com.zhuoxin.treasure.custom.AlertDialogFragment;
import com.zhuoxin.treasure.login.LoginActivity;
import com.zhuoxin.treasure.map.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_Username)
    EditText etUsername;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.et_Confirm)
    EditText etConfirm;
    @BindView(R.id.btn_Register)
    Button btnRegister;
    private Unbinder mUnbind;
    private ActivityUtils mActivityUtils;
    private String mUserName;
    private String mPassWord;
    private String mConfirm;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUnbind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.register);
        }
        etConfirm.addTextChangedListener(mTexyWatcher);
        etPassword.addTextChangedListener(mTexyWatcher);
        etUsername.addTextChangedListener(mTexyWatcher);
    }
   private TextWatcher mTexyWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mUserName = etUsername.getText().toString();
            mPassWord = etPassword.getText().toString();
            mConfirm = etConfirm.getText().toString();
            boolean canRegister=!TextUtils.isEmpty(mUserName)&&!TextUtils.isEmpty(mPassWord)&&!TextUtils.isEmpty(mConfirm);
            btnRegister.setEnabled(canRegister);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
        if (RegexUtils.verifyUsername(mUserName)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getinstance(getString(R.string.username_error),getString(R.string.username_rules))
            .show(getSupportFragmentManager(),"username_error");
            return;
        }
        if (RegexUtils.verifyPassword(mPassWord)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getinstance(getString(R.string.password_error),getString(R.string.password_rules))
                    .show(getSupportFragmentManager(),"password_error");
            return;
        }
        if (!mPassWord.equals(mConfirm)){
            AlertDialogFragment.getinstance("密码确认错误","两次输入的密码不同")
                    .show(getSupportFragmentManager(),"密码确认错误");
            return;
        }
        new RegisterPresenter(this).register(new User(mUserName,mPassWord));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(RegisterActivity.this, "注册", "正在注册,请稍后...");
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
        LocalBroadcastManager.getInstance(RegisterActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_MAIN));
    }
}
