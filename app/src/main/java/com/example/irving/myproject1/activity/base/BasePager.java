package com.example.irving.myproject1.activity.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Irving on 2017/10/24.
 * 基类、公共类
 */

public abstract class BasePager {
    public final Context context;
    public View rootView;
    public boolean isStartPager;  //默认为false

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 初始化视图页面
     * @return
     */
    public abstract View initView();

    /**
     * 实现视图页面的数据，联网请求
     */
    public void initData(){

    }

}
