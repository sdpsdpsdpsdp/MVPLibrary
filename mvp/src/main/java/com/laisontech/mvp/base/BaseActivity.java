package com.laisontech.mvp.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.laisontech.mvp.CustomCircleDialog;
import com.laisontech.mvp.R;
import com.laisontech.mvp.utils.ActionBarSetter;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by SDP on 2017/4/12.
 * base基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnTouchListener {
    protected boolean isNeedOnKeyDown = false;
    private Toast mToast;
    private CustomCircleDialog mDialogWaiting;
    private float mFirstTouchY;
    private float mTouchSlop;
    protected static final int SCAN_CODE = 0;
    private long currentTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //增加到activity的栈集合中
        //处理在加载布局前需要设置的方法功能
        setOtherMethodBeforeLoadingContentView();
        //填充布局
        setContentView(setContentViewID());
        //初始化控件(可以使用ButterKnife、也可以不使用)
        ButterKnife.bind(this);
        getTouchSlop();
        //加载资源
        initData(savedInstanceState);
        //加载资源
        initData();
        //加载资源
        initData(true);
        //控件的操作事件
        initEvent();
    }


    //处理在加载布局前需要设置的方法等 无需抽象，子类可以不是实现
    protected void setOtherMethodBeforeLoadingContentView() {
    }

    //填充布局
    protected abstract
    @LayoutRes
    int setContentViewID();

    private void getTouchSlop() {
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    //初始化数据源
    protected void initData() {

    }
    //初始化数据源
    protected void initData(Bundle savedInstanceState) {
    }

    //初始化数据源
    protected void initData(boolean isNeedRefresh) {
    }

    //初始化事件
    protected void initEvent() {

    }

    //传入Bundle的方式打开activity
    protected void openActivity(Bundle bundle, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String keyName, String valueName, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(keyName, valueName);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String keyName, boolean valueName, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(keyName, valueName);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String parcelableName, Parcelable parcelable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(parcelableName, parcelable);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String serializableName, Serializable serializable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(serializableName, serializable);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    //startActivityForResult
    protected void openActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    //startActivityForResult 传入Bundle
    protected void openActivityForResult(Bundle bundle, Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 使用反射打开activity
     *
     * @param activityName 要打开的activity全路径 com.laisontech.xxx.xxxActivity
     * @param intent       传参使用的Intent
     */
    public void navigatorTo(final String activityName, final Intent intent) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(activityName);
            if (clazz != null) {
                intent.setClass(this, clazz);
                this.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //弹出toast
    @SuppressLint("ShowToast")
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    protected void showToast(int msgId) {
        String msg = getResources().getString(msgId);
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    //设置自定义的actionBar,子类只需要复写此方法即可
    protected void setCustomActionBar(final AppCompatActivity activity, Object obj) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setCustomActionBar(activity);
        actionBarView.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getWindow().peekDecorView();
                if (view != null) {
                    closeInputWindow();
                }
                activity.finish();
            }
        });
        setTvRes(obj, actionBarView);
    }

    //设置自定义的actionBar,子类只需要复写此方法即可
    protected void setCustomActionBar(final AppCompatActivity activity, Object obj,int textSize) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setCustomActionBar(activity);
        actionBarView.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getWindow().peekDecorView();
                if (view != null) {
                    closeInputWindow();
                }
                activity.finish();
            }
        });
        setTvRes(obj, actionBarView);
    }

    //设置自定义的actionBar,子类只需要复写此方法即可
    protected void setCustomActionBar(final AppCompatActivity activity, Object obj, int rightResID, final View.OnClickListener listener) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setCustomActionBar(activity);
        actionBarView.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInputWindow();
                activity.finish();
            }
        });

        actionBarView.rightIv.setImageResource(rightResID);
        actionBarView.rightIv.setVisibility(View.VISIBLE);
        actionBarView.rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInputWindow();
                listener.onClick(v);
            }
        });
        setTvRes(obj, actionBarView);
    }
    protected void setMiddleActionBar(final AppCompatActivity activity, Object obj) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setMiddleActionBar(activity);
        setTvRes(obj, actionBarView);
    }


    //设置自定义的actionBar,子类只需要复写此方法即可
    //增加按钮事件
    protected void setCustomActionBar(final AppCompatActivity activity, Object obj, View.OnClickListener onClickListener) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setCustomActionBar(activity);
        actionBarView.ll.setOnClickListener(onClickListener);
        setTvRes(obj, actionBarView);
    }

    //关闭文本输入框
    private void closeInputWindow() {
        View view = this.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void setTvRes(Object obj, ActionBarSetter.ActionBarView actionBarView) {
        if (obj instanceof Integer) {
            actionBarView.tv.setText(getResStr((int) obj));
        } else if (obj instanceof String) {
            actionBarView.tv.setText((String) obj);
        }
    }

    protected void setBackgroundAlpha(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    protected String getResStr(int resId) {
        return getResources().getString(resId);
    }

    protected String getEditTextStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    //不可取消的dialog
    protected void showWaitingDialog(String tip) {
        showWaitingDialog(tip, false);
    }

    protected void showWaitingDialog(int tipId) {
        showWaitingDialog(getResStr(tipId), false);
    }

    /**
     * 显示等待提示框
     */
    protected void showWaitingDialog(String tip, boolean needCancelable) {
        if (mDialogWaiting == null) {
            mDialogWaiting = new CustomCircleDialog(this, R.style.CustomCircleDialog);
        }
        View view = View.inflate(this, R.layout.include_dialog_waiting, null);
        ((TextView) view.findViewById(R.id.tv_load_title)).setText(tip);
        mDialogWaiting.setView(view);
        mDialogWaiting.setCanceledOnTouchOutside(false);
        mDialogWaiting.setCancelable(needCancelable);
        if (!mDialogWaiting.isShowing()) {
            mDialogWaiting.show();
        }
    }

    /**
     * 隐藏等待提示框
     */
    protected void hideWaitingDialog() {
        if (mDialogWaiting != null && mDialogWaiting.isShowing()) {
            mDialogWaiting.dismiss();
        }
    }

    //设置App字体大小不变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentTouchY = event.getY();
                if (currentTouchY - mFirstTouchY > mTouchSlop) {
                    //下滑动
                    onScreenMoveDownload();
                } else if (mFirstTouchY - currentTouchY > mTouchSlop) {
                    //上滑动
                    onScreenMoveUp();
                }
                onActionMove();
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
        }
        return false;
    }

    protected void onScreenMoveDownload() {
    }


    protected void onScreenMoveUp() {
    }

    protected void onActionMove() {
    }

    protected void onActionUp() {
    }

    protected void onScanQCode(String result) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        String result = extras.getString("result");
                        if (result != null) {
                            onScanQCode(result);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isNeedOnKeyDown) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - currentTime > 2000) {
                    showToast(R.string.exit);
                    currentTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialogWaiting = null;
    }
}

