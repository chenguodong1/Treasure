package com.zhuoxin.treasure.register;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void showMessage(String message);
    void navigateToHomeActivity();
}
