package com.laisontech.mvplibrary.login;


import com.laisontech.mvp.mvp.BasePresenter;
import com.laisontech.mvp.mvp.BaseView;

/**
 * 登录接口类的连接类
 */

public class LoginContract {
    interface View extends BaseView {
        void loginSuccess(String successMsg);

        void loginFailed(String failMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void login(String uid, String pwd);
    }
}
