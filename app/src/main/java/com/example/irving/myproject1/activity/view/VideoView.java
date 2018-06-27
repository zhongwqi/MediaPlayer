package com.example.irving.myproject1.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Irving on 2017/12/1.
 */

public class VideoView extends android.widget.VideoView {
    /**
     * 在代码中创建的时候一般回调这个方法
     * @param context
     */
    public VideoView(Context context) {
        super(context);
    }

    /**
     * 当这个类在布局文件的时候，系统通过该构造方法实例化该类
     * @param context
     * @param attrs
     */
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 当需要设置样式的时候调用这个方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写测量方法 重新测量控件的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 保存测量的控件的结果
         */
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 用来设置屏幕的大小
     */
    public void setVideoSize(int width,int height){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }
}
