package com.example.irving.myproject1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;


import com.example.irving.myproject1.R;

/**
 * Created by Irving on 2017/10/22.
 */

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getCanonicalName();//"SplashActivity"
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //在主线程中执行，Runnable在哪里创建，线程就在哪个线程中执行
                startMainActivity();//延时2秒后跳转到主线程
            }
        },2000);
    }
    /*
    从登录页面触摸进入主页面，有两种方法：
    1.在功能清单文件中设置登录模式为singerTask
    2.从源头startMainActivity()方法中设置只跳转一次页面
     */

    boolean isStartMainActivity = false;
    private void startMainActivity() {
        if(!isStartMainActivity){
            isStartMainActivity = true;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();//关闭当前页面
        }

    }

    /**
     * 触摸进入主页面
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getAction() );
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        //把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
