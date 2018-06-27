package com.example.irving.myproject1.activity.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.irving.myproject1.activity.base.BasePager;

import static com.example.irving.myproject1.activity.pager.AudioPager.TAG;

/**
 * Created by Irving on 2017/10/24.
 * 网络视频页面
 */

public class NetAudioPager extends BasePager{
    public static final String TAG = "NetAudioPager";
    private TextView textView;
    public NetAudioPager(Context context) {
        super(context);
    }

    //实现网络视频页面的初始化
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(16);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    //实现网络视频页面的数据的
    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "initData: "+ "网络音频页面被初始化了" );
        textView.setText("网络音频页面");
    }
}
