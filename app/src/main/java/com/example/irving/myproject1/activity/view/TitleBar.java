package com.example.irving.myproject1.activity.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irving.myproject1.R;

/**
 * Created by Irving on 2017/10/25.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    /**
     * 当代码中实例化该类的时候使用这个方法
     * @param context
     */
    private TextView tv_titleBar;
    private RelativeLayout seach_titleBar;
    private ImageView iv_record;
    private Context context;

    public TitleBar(Context context) {
        this(context,null);
    }

    /**
     * 当在布局文件使用该类的时候，系统通过这个构造方法实例化该类
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 当需要样式的时候，可以使用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 当布局文件加载完成时，回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

         /*
        得到孩子的实例
         */
        tv_titleBar = (TextView) getChildAt(1);
        seach_titleBar = (RelativeLayout) getChildAt(2);
        iv_record = (ImageView) getChildAt(3);

        //设置点击事件
        tv_titleBar.setOnClickListener(this);
        seach_titleBar.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_titleBar:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;

            case R.id.seach_titleBar:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_record:
                Toast.makeText(context, "历史", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
