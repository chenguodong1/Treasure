package com.zhuoxin.treasure.login;



import com.zhuoxin.treasure.User;
import com.zhuoxin.treasure.UserPrefs;
import com.zhuoxin.treasure.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public class LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    public void login(User user){
        mLoginView.showProgress();
        NetClient.getInstance().getNetRequest().login(user).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                mLoginView.hideProgress();
                if (response.isSuccessful()){
                    UserResult userResult = response.body();
                    if (userResult==null){
                        mLoginView.showMessage("未知错误！");
                        return;
                    }
                    if (userResult.getErrcode()!=1){
                        mLoginView.showMessage(userResult.getErrmsg());
                        return;
                    }

                    String headpic = userResult.getHeadpic();
                    int tokenid = userResult.getTokenid();
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+headpic);
                    UserPrefs.getInstance().setTokenid(tokenid);
                    mLoginView.navigateToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                mLoginView.hideProgress();
                mLoginView.showMessage("请求失败"+t.getMessage());
            }
        });
    }
}
