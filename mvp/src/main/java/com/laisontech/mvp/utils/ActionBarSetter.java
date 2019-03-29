package com.laisontech.mvp.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.mvp.R;

import java.util.Locale;

/**
 * Created by SDP on 2017/4/12.
 * 设置自定义的ActionBar
 * 需注意的是，使用该自定义的ActionBar必须在AppcompatActivity中使用
 */
public class ActionBarSetter {
    //设置自定义的actionBar
    public static ActionBarView setCustomActionBar(AppCompatActivity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_actionbar, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_actionBar_back);
        TextView tv = (TextView) view.findViewById(R.id.tv_actionBar_title);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_actionBar_back);
        ImageView rightIv = (ImageView) view.findViewById(R.id.iv_right);
        //初始化ActionBarView
        ActionBarView viewList = new ActionBarView();
        viewList.tv = tv;
        viewList.iv = iv;
        viewList.rightIv = rightIv;
        viewList.ll = ll;
        String language = Locale.getDefault().getLanguage();
        if (language.equals("ar") || language.equals("fa")) {
            viewList.iv.setImageResource(R.drawable.actionbar_iv_fa_selector);
        }
        setParams(activity, view);

        return viewList;
    }

    //设置自定义的actionBar
    public static ActionBarView setCustomActionBar(AppCompatActivity activity, int textSize) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_actionbar, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_actionBar_back);
        TextView tv = (TextView) view.findViewById(R.id.tv_actionBar_title);
        tv.setTextSize(sp2px(activity, textSize));
        ImageView iv = (ImageView) view.findViewById(R.id.iv_actionBar_back);
        ImageView rightIv = (ImageView) view.findViewById(R.id.iv_right);
        //初始化ActionBarView
        ActionBarView viewList = new ActionBarView();
        viewList.tv = tv;
        viewList.iv = iv;
        viewList.rightIv = rightIv;
        viewList.ll = ll;
        String language = Locale.getDefault().getLanguage();
        if (language.equals("ar") || language.equals("fa")) {
            viewList.iv.setImageResource(R.drawable.actionbar_iv_fa_selector);
        }
        setParams(activity, view);

        return viewList;
    }

    //设置其他布局下的actionBar
    public static ActionBarView setMiddleActionBar(AppCompatActivity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_text_actionbar, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        //初始化ActionBarView
        ActionBarView viewList = new ActionBarView();
        viewList.tv = tv;
        setParams(activity, view);
        return viewList;
    }

    private static void setParams(AppCompatActivity activity, View view) {
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ActionBar actionBar = setActionBarParams(activity);
        actionBar.setCustomView(view, params);
    }

    private static ActionBar setActionBarParams(AppCompatActivity activity) {
        //获取ActionBar对象
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        //设置ActionBar可以自定义
        actionBar.setDisplayShowCustomEnabled(true);
        //设置actionBar的显示选项为自定义
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        return actionBar;
    }

    public static class ActionBarView {
        public LinearLayout ll;
        ImageView iv;
        public ImageView rightIv;
        public TextView tv;
    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
