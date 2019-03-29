package com.laisontech.mvplibrary.login;


import com.laisontech.mvp.mvp.BasePresenterImpl;

/**
 * 执行登录的耗时
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter, LoginModel.OnLoginListener {
    private LoginModel model;

    public LoginPresenter() {
        this.model = new LoginModel();
    }

    @Override
    public void login(String uid, String pwd) {
    }

    @Override
    public void success(String successMsg) {
        mView.loginSuccess(successMsg);
    }

    @Override
    public void failed(String failedMsg) {
        mView.loginFailed(failedMsg);
    }
}
