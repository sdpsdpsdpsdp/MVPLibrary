package com.laisontech.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.laisontech.mvp.CustomCircleDialog;
import com.laisontech.mvp.R;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by SDP on 2017/8/16.
 */

public abstract class BaseFragment extends Fragment {
    private CustomCircleDialog mDialogWaiting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract
    @LayoutRes
    int getResLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initEvent();
    }

    protected void initData() {
    }

    protected void initEvent() {
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String serializableName, Serializable serializable, Class<?> clz) {
        Intent intent = new Intent(getContext(), clz);
        intent.putExtra(serializableName, serializable);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(Class<?> clz) {
        Intent intent = new Intent(getContext(), clz);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(String key, boolean values, Class<?> clz) {
        Intent intent = new Intent(getContext(), clz);
        intent.putExtra(key, values);
        startActivity(intent);
    }

    //startActivityForResult
    protected void openActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(getContext(), clz);
        startActivityForResult(intent, requestCode);
    }

    //startActivityForResult 传入Bundle
    protected void openActivityForResult(Bundle bundle, Class<?> clz, int requestCode) {
        Intent intent = new Intent(getContext(), clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    //不可取消的dialog
    protected void showWaitDialog(String tip) {
        showWaitDialog(tip, false);
    }

    protected void showWaitDialog(int tipId) {
        showWaitDialog(getResStr(tipId), false);
    }

    /**
     * 显示等待提示框
     */
    protected void showWaitDialog(String tip, boolean needCancelable) {
        if (mDialogWaiting == null) {
            mDialogWaiting = new CustomCircleDialog(getContext(), R.style.CustomCircleDialog);
        }
        View view = View.inflate(getContext(), R.layout.include_dialog_waiting, null);
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
    protected void hideWaitDialog() {
        if (mDialogWaiting != null && mDialogWaiting.isShowing()) {
            mDialogWaiting.dismiss();
        }
    }

    protected String getResStr(int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDialogWaiting = null;
    }
}
