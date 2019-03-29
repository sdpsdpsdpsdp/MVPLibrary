package com.laisontech.mvplibrary.login;

/**
 * Created by SDP on 2018/4/13.
 */

public class LoginModel {
    private String url = "http://laisontechsoft.xicp.net:18082/?function=login&operatoruid=Panda&operatorpwd=4bcbbbc00b0341b4b413cffc9eb02208";
    private String url2 ="http://laisontechsoft.xicp.net:18082/?function=maintainerlogin&accountname=Panda&password=4bcbbbc00b0341b4b413cffc9eb02208";
    interface OnLoginListener {
        void success(String successMsg);

        void failed(String failedMsg);
    }

}
