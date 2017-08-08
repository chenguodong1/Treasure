package com.zhuoxin.treasure.register;

import com.zhuoxin.treasure.User;
import com.zhuoxin.treasure.UserPrefs;
import com.zhuoxin.treasure.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/2.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }
    public void register(User user){
        mRegisterView.showProgress();
        NetClient.getInstance().getNetRequest().register(user).enqueue(new Callback<RegistResult>() {
            @Override
            public void onResponse(Call<RegistResult> call, Response<RegistResult> response) {
                mRegisterView.hideProgress();
                if (response.isSuccessful()) {
                    RegistResult result = response.body();
                    if (result == null) {
                        mRegisterView.showMessage("未知错误");
                        return;
                    }
                    switch (result.getErrcode()){
                        case 1:
                        case 2:
                            mRegisterView.showMessage(result.getErrmsg());
                            break;
                        default:
                            mRegisterView.showMessage("默认错误");
                    }
                    UserPrefs.getInstance().setTokenid(result.getTokenid());
                    mRegisterView.navigateToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<RegistResult> call, Throwable t) {
                mRegisterView.hideProgress();
                mRegisterView.showMessage("获取数据失败");
            }
        });
    }
}

