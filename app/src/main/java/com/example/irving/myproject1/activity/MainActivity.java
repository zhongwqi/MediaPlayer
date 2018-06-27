package com.example.irving.myproject1.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.irving.myproject1.R;
import com.example.irving.myproject1.activity.base.BasePager;
import com.example.irving.myproject1.activity.fragment.ReplaceFragment;
import com.example.irving.myproject1.activity.pager.AudioPager;
import com.example.irving.myproject1.activity.pager.NetAudioPager;
import com.example.irving.myproject1.activity.pager.NetVideoPager;
import com.example.irving.myproject1.activity.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private FrameLayout fl_main_content;
    private RadioGroup rg_main;
    //页面的集合
    private ArrayList<BasePager> basePagers;
    //选中的位置
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);


        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));

        //设置RadioGroup的点击事件
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //设置默认的选中的本地视频
        rg_main.check(R.id.rb_video);
    }

    private void setFragment() {
        /**
         * 1.得到事务管理者
         * 2.开启事务
         * 3.替换
         * 4.提交事务
         */
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.fl_main_content,new ReplaceFragment(getBasePager()));

        ft.commit();
    }


    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if(basePager != null && !basePager.isStartPager){ //这里不加上！basePager.isStartPager会导致list View加载错乱的问题
            basePager.isStartPager = true;
            basePager.initData();
        }
        return basePager;
    }


    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){

                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_NetVideoPager:
                    position = 2;
                    break;
                case R.id.rb_NetAudioPager:
                    position = 3;
                    break;
                default:
                    //默认选中的位置
                    position = 0;
                    break;
            }

            setFragment();
        }
    }
}
