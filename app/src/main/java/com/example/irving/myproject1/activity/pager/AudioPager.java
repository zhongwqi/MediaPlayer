package com.example.irving.myproject1.activity.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.irving.myproject1.activity.base.BasePager;

import static android.content.ContentValues.TAG;

/**
 * Created by Irving on 2017/10/24.
 * 本地音频页面
 */

public class AudioPager extends BasePager{
    public static final String TAG = "AudioPager";
    private TextView textView;
    public AudioPager(Context context) {
        super(context);
    }

    //实现本地音频页面页面的初始化
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(16);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    //实现本地音频页面页面的数据的
    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "initData: "+ "本地音频页面被初始化了" );
        textView.setText("本地音频页面");
    }
}
