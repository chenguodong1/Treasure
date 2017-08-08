package com.zhuoxin.treasure.login;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public interface LoginView {
    void showProgress();
    void hideProgress();
    void showMessage(String message);
    void navigateToHomeActivity();
}
