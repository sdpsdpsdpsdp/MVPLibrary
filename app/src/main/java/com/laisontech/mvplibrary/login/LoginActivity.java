package com.laisontech.mvplibrary.login;


import android.util.Log;
import android.widget.EditText;

import com.laisontech.mvp.base.BaseActivity;
import com.laisontech.mvp.mvp.MVPBaseActivity;
import com.laisontech.mvp.net.OnConnectResultListener;
import com.laisontech.mvp.net.okconnect.OkHttpConnect;
import com.laisontech.mvp.net.okserver.okhttp.OkServer;
import com.laisontech.mvp.net.okserver.okhttp.callback.AbsCallback;
import com.laisontech.mvp.net.okserver.okhttp.callback.Callback;
import com.laisontech.mvp.net.okserver.okhttp.callback.StringCallback;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;
import com.laisontech.mvp.net.okserver.okhttp.model.Response;
import com.laisontech.mvp.net.okserver.okhttp.request.base.Request;
import com.laisontech.mvplibrary.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_uid)
    EditText etUid;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    @Override
    protected int setContentViewID() {
        return R.layout.activity_main;
    }


    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        queryLogin();
        queryTask();
    }

    private void queryLogin() {
        String url = baseGetUrl() + "maintainerlogin" +
                "&" + "techniciannumber" +
                "=" + "panda" +
                "&" + "password" +
                "=" + "433c24d7ec7ed9c8ad7e4218d4742256";
        OkHttpConnect.getInstance().buildGetString(url, "login", new OnConnectResultListener() {
            @Override
            public void onResponse(String response, Object tag) {
                    Log.e("queryNet", "onResponse:: queryLogin--> " + response + ",tag：" + tag.toString());
            }

            @Override
            public void onError(String errorMsg, Object tag) {
                    Log.e("queryNet", "onError: queryLogin-->" + errorMsg + ",tag：" + tag.toString());
            }
        });
    }

    private String baseGetUrl() {
        return "http://192.168.0.155:28206/" + "?" + "function" + "=";
    }


    private void queryTask() {
        String url = "http://192.168.0.155:28205/" + "?" + "function" + "=" + "getdcucommunicationtesttaskdescinfo" +
                "&" + "techniciannumber" +
                "=" + "panda";
        OkHttpConnect.getInstance().buildGetString(url, "task", new OnConnectResultListener() {
            @Override
            public void onResponse(String s, Object tag) {
                    Log.e("queryNet", "onResponse:: queryTask--> " + s + ",tag：" + tag.toString());

            }

            @Override
            public void onError(String s, Object tag) {

                    Log.e("queryNet", "onError: queryTask-->" + s + ",tag：" + tag.toString());
            }
        });
    }
}
