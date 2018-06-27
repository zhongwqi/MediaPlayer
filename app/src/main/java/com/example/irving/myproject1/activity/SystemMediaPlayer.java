package com.example.irving.myproject1.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.irving.myproject1.R;
import com.example.irving.myproject1.activity.domain.MediaItem;
import com.example.irving.myproject1.activity.utils.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Irving on 2017/10/28.
 */

public class SystemMediaPlayer extends Activity implements View.OnClickListener {
    public static final int PROCESS = 1;
    private static final int HIDE_MEDIACONTROLLER = 2;
    /**
     * 默认屏幕
     */
    public static final int Default_screen = 1;
    /**
     * 全屏
     */
    public static final int Full_screen = 2;
    private LinearLayout llPlayerTop;
    private TextView tvName;
    private ImageView btnBattery;
    private TextView tvTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private ImageView btnSwitchPlayer;
    private LinearLayout llPlayerBottom;
    private SeekBar seekbarPlay;
    private Button btnExitVideo;
    private Button btnPerVideo;
    private Button btnPlayVideo;
    private Button btnNextVideo;
    private Button btnSwitchScreenState;
    private Uri uri;
    private Utils utils;
    private TextView tv_currentDuration;
    private TextView tv_duration;
    //监听电量的广播
    private BroadcastReceiver receiver;
    //传递过来的视频列表
    private ArrayList<MediaItem> mediaItems;
    //视频在列表中的位置
    private int videoposition;
    //注册手势识别器
    private GestureDetector detector;
    //控制面板
    private RelativeLayout media_controller;
    //是否显示控制面板
    private boolean isShowMediaController = false;
    /**
     * 屏幕的宽
     */
    private int screenWidth = 0;
    /**
     * 屏幕的高
     */
    private int screenHeight = 0;
    /**
     * 视频真实的宽
     */
    private int videoWidth = 0;
    /**
     * 视频真实的高
     */
    private int videoHeight = 0;
    /**
     * 自定义的VideoView
     */
    private com.example.irving.myproject1.activity.view.VideoView videoview;
    /**
     * 当前屏幕显示类型是否为全屏
     */
    private boolean isFullScreen = false;
    /**
     * 调节音量
     */
    private AudioManager audioManager;
    /**
     * 当前音量
     */
    private int currentVoice;
    /**
     * 最大音量
     */
    private int maxVoice;
    /**
     * 是否是静音
     */
    private boolean isMute = false;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-11-27 21:04:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        media_controller = (RelativeLayout) findViewById(R.id.media_controller);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_currentDuration = (TextView) findViewById(R.id.tv_currentDuration);
        llPlayerTop = (LinearLayout)findViewById( R.id.ll_player_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        btnBattery = (ImageView)findViewById( R.id.btn_battery );
        tvTime = (TextView)findViewById( R.id.tv_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (ImageView)findViewById( R.id.btn_switch_player );
        llPlayerBottom = (LinearLayout)findViewById( R.id.ll_player_bottom );
        seekbarPlay = (SeekBar)findViewById( R.id.seekbar_play );
        btnExitVideo = (Button)findViewById( R.id.btn_exit_video );
        btnPerVideo = (Button)findViewById( R.id.btn_per_video );
        btnPlayVideo = (Button)findViewById( R.id.btn_play_video );
        btnNextVideo = (Button)findViewById( R.id.btn_next_video );
        btnSwitchScreenState = (Button)findViewById( R.id.btn_switch_screen_state );
        videoview = (com.example.irving.myproject1.activity.view.VideoView) findViewById(R.id.videoview);

        btnVoice.setOnClickListener(this);
        btnExitVideo.setOnClickListener(this);
        btnPerVideo.setOnClickListener( this );
        btnPlayVideo.setOnClickListener( this );
        btnNextVideo.setOnClickListener( this );
        btnSwitchScreenState.setOnClickListener( this );
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_media_player);
        findViews();
        initData();
        setVideoListener();

        //得到播放地址
        getData();
       setData();
        setButtonState();

       // videoview.setMediaController(new MediaController(this));
    }

    private void getData(){
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("vidiolist");
        videoposition = getIntent().getIntExtra("videoposition",0);
    }

    private void setData(){
        uri = getIntent().getData();

        if(mediaItems != null && mediaItems.size() > 0){
            //列表存在则优先播放列表
            MediaItem mediaItem = mediaItems.get(videoposition);
            //显示视频名称
            tvName.setText(mediaItem.getName());
            //设置视频的路径
            videoview.setVideoPath(mediaItem.getData());
        }else if(uri != null){
            tvName.setText(uri.toString());
            videoview.setVideoURI(uri);
        }else{
            Toast.makeText(this, "您没有传递数据", Toast.LENGTH_LONG).show();
        }
    }
    private void initData(){
        utils = new Utils();
        //注册电量广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加监听电量变化的动作
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);

        //实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            /**
             * 长按屏幕时回调这个方法
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
//                Toast.makeText(SystemMediaPlayer.this, "我被长按了", Toast.LENGTH_SHORT).show();
                if(isFullScreen){//全屏
                    setScreenStyle(Default_screen);
                }else{//默认屏幕
                    setScreenStyle(Full_screen);
                }
            }


            /**
             * 双击屏幕时回调这个方法
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(SystemMediaPlayer.this, "我被双击了", Toast.LENGTH_SHORT).show();
                //双击屏幕实现播放和暂停的切换
                startAndPause();
                return super.onDoubleTap(e);
            }

            /**
             * 单击屏幕时回调这个方法
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
//              Toast.makeText(SystemMediaPlayer.this, "我被单击了", Toast.LENGTH_SHORT).show();

                if(isShowMediaController){
                    //隐藏控制面板
                    hideMediaController();
                    //移除消息
                    handler.removeMessages(HIDE_MEDIACONTROLLER);

                }else{
                    //显示控制面板
                    showMediaController();
                    //发送消息延时隐藏面板
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);

                }
                return super.onSingleTapConfirmed(e);
            }
        });

        /**
         * 得到屏幕的尺寸
         * （过时方式）
         */
//        getWindowManager().getDefaultDisplay().getWidth();
//        getWindowManager().getDefaultDisplay().getHeight();

        /**
         * 得到屏幕的尺寸
         * （最新方式）
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();//实例化
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        /**
         * 实例化AudioManager
         */
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void setScreenStyle(int defaultScreen){
        switch (defaultScreen){
            case Default_screen:
                //1.设置默认屏幕
                int width = screenWidth;
                int height = screenHeight;
                if(videoWidth * screenHeight < videoHeight * screenWidth){
                    width = videoWidth * screenHeight / videoHeight;
                }else if(videoWidth * screenHeight > videoHeight * screenWidth){
                    height = videoHeight * screenWidth / videoWidth;
                }
                videoview.setVideoSize(width,height);
                //2.设置按钮状态--全屏
                btnSwitchScreenState.setBackgroundResource(R.drawable.btn_switch_full_screendrawable_selector);
                isFullScreen = false;
                break;
            case Full_screen:
                //1.设置全屏
                videoview.setVideoSize(screenWidth,screenHeight);
                //2.设置按钮状态--默认
                btnSwitchScreenState.setBackgroundResource(R.drawable.btn_switch_screen_state_drawable_selector);
                isFullScreen = true;
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);//默认值为0
            setBatteryLevel(level);
        }
    }

    private void setBatteryLevel(int level){
        if(level <= 0){
            btnBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level <= 10){
            btnBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level <= 20){
            btnBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level <= 40){
            btnBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level <= 60){
            btnBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level <= 80){
            btnBattery.setImageResource(R.drawable.ic_battery_80);
        }else{
            btnBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setVideoListener(){
        //播放准备好的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        //播放出错的监听
        videoview.setOnErrorListener(new MyOnErrorListener());

        //播放完成的监听
        videoview.setOnCompletionListener(new MyOnCompletionListener());

        //设置SeekBar状态变化的监听
        seekbarPlay.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

        //设置音量变化的监听
        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
    }

    /**
     * 设置音量的状态
     * @param progress
     */
    private void setVoiceState(int progress,boolean isMute){
        if(isMute){//静音
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);//最后一个参数为1--显示系统音量 参数为0--不显示
            seekbarVoice.setProgress(0);
        }else{
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);//最后一个参数为1--显示系统音量 参数为0--不显示
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }

    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                if(progress > 0){
                    isMute = false;
                }else{
                    isMute = true;
                }
                setVoiceState(progress,isMute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
          handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,4000);
        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        public VideoOnSeekBarChangeListener() {
            super();
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        /**
         * 当SeekBar状态发生改变的时候回调这个方法
         * @param seekBar
         * @param progress
         * @param fromUser
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//fromUser默认为false，拖拽的时候为true
            if(fromUser){
                videoview.seekTo(progress);
            }
        }

        /**
         * 当拖拽SeekBar的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
               handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        /**
         * 当SeekBar拖拽借书的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
               handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                    break;
                case PROCESS:
                    int currentTime = videoview.getCurrentPosition();

                    seekbarPlay.setProgress(currentTime);

                    //更新视频播放进度时间
                    tv_currentDuration.setText(utils.stringForTime(currentTime));

                    //每秒更新系统时间
                    tvTime.setText(getSystemTime());

                    handler.removeMessages(PROCESS);
                    //每秒更新视频的进度

                    handler.sendEmptyMessageDelayed(PROCESS,1000);
            }
        }
    };

    private String getSystemTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(new Date());//每秒创建一个Date对象，创建新的对象没有调用，很快会释放
    }


    private class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            videoview.start();//开始播放视频

            //mp.getDuration();//得到视频的总时长
            int duration = videoview.getDuration();

            seekbarPlay.setMax(duration);

            tv_duration.setText(utils.stringForTime(duration));

            //默认隐藏控制面板
            hideMediaController();
            handler.sendEmptyMessage(PROCESS);
            //设置默认屏幕
            setScreenStyle(Default_screen);
            //设置最大音量 1-15
            seekbarVoice.setMax(maxVoice);
            //设置当前音量
            seekbarVoice.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    private class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            Toast.makeText(SystemMediaPlayer.this,"播放出错了哦",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
//            Toast.makeText(SystemMediaPlayer.this, "播放完成了", Toast.LENGTH_LONG).show();
            //设置自动播放下一个视频
            Toast.makeText(SystemMediaPlayer.this, "自动播放下一个视频", Toast.LENGTH_SHORT).show();
            setNextVideo();
        }
    }



    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-11-27 21:04:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnExitVideo ) {//退出
            // Handle clicks for btnExitVideo
            finish();
        } else if ( v == btnPerVideo ) {//上一个视频
            // Handle clicks for btnPerVideo
            if(mediaItems != null && mediaItems.size() > 0){
                videoposition--;
                if(videoposition >=0){
                    MediaItem mediaItem = mediaItems.get(videoposition);
                    tvName.setText(mediaItem.getName());
                    videoview.setVideoPath(mediaItem.getData());
                    setButtonState();
                }
            }
        } else if ( v == btnPlayVideo ) {//播放/暂停
            // Handle clicks for btnPlayVideo
            startAndPause();
        } else if ( v == btnNextVideo ) {//下一个视频
            // Handle clicks for btnNextVideo
           setNextVideo();
        } else if ( v == btnSwitchScreenState ) {//切换屏幕的状态
            // Handle clicks for btnSwitchScreenState
        }else if(v == btnVoice){//声音
            // Handle clicks for btnVoice
            isMute = !isMute;
            setVoiceState(currentVoice,isMute);

        }

        handler.removeMessages(HIDE_MEDIACONTROLLER);//移除旧的消息
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);//创建新的消息
    }

    /**
     * 设置播放下一个视频
     */
    private void setNextVideo(){
        if(mediaItems != null && mediaItems.size() > 0) {
            videoposition++;
            if (videoposition < mediaItems.size()) {//注意这里是<
                MediaItem mediaItem = mediaItems.get(videoposition);
                tvName.setText(mediaItem.getName());
                videoview.setVideoPath(mediaItem.getData());
                setButtonState();
            }
        }
    }

    private void startAndPause(){
        if(videoview.isPlaying()){
            //视频正在播放-设置暂停
            videoview.pause();
            //设置按钮为播放
            btnPlayVideo.setBackgroundResource(R.drawable.btn_play_video_drawable_selector);
        }else{
            //视频暂停播放
            videoview.start();
            //设置按钮为播放
            btnPlayVideo.setBackgroundResource(R.drawable.btn_pause_video_drawable_selector);
        }
    }

    private void setButtonState(){
        if(mediaItems != null && mediaItems.size() > 0){
            if(mediaItems.size() == 1) {//mediaItems.size() == 1
                //只有1个视频
                setOnlyVideoState();
            }else{
                //有2个以上的视频
                if(videoposition == 0){
                   setFirstVideoState();
                }else if(videoposition == mediaItems.size()-1){
                    setLastVideoState();
                }else{
                    btnPerVideo.setBackgroundResource(R.drawable.btn_per_video_drawable_selector);
                    btnPerVideo.setEnabled(true);
                    btnNextVideo.setBackgroundResource(R.drawable.btn_next_video_drawable_selector);
                    btnNextVideo.setEnabled(true);
                }
            }
        }else if(uri != null){
            setOnlyVideoState();
        }
    }

    private void setFirstVideoState(){
        btnPerVideo.setBackgroundResource(R.drawable.btn_pre_normal);
        btnPerVideo.setEnabled(false);
        btnNextVideo.setBackgroundResource(R.drawable.btn_next_video_drawable_selector);
        btnNextVideo.setEnabled(true);
    }

    private void setLastVideoState(){
        btnPerVideo.setBackgroundResource(R.drawable.btn_per_video_drawable_selector);
        btnPerVideo.setEnabled(true);
        btnNextVideo.setBackgroundResource(R.drawable.btn_next_normal);
        btnNextVideo.setEnabled(false);
    }

    private void setOnlyVideoState(){
        btnPerVideo.setBackgroundResource(R.drawable.btn_pre_normal);
        btnPerVideo.setEnabled(false);
        btnNextVideo.setBackgroundResource(R.drawable.btn_next_normal);
        btnNextVideo.setEnabled(false);
    }

    @Override
    protected void onDestroy() {//取消注册，先释放子类，其次再释放父类  避免空值异常
        if(receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    private float startY;
    private float startX;
    /**
     * 屏幕的高
     */
    private float touchRang;
    /**
     * 按下屏幕时的音量
     */
    private int mVol;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //传递事件给手势识别器
        detector.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN://手指按下
                //1.按下记录值
                startY = event.getY();
                startX = event.getX();
                mVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screenHeight,screenWidth);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE://手指移动
                //2.移动的记录相关值
                float endY = event.getY();
                float endX = event.getX();
                float distanceY = startY - endY;
                //改变声音 = （滑动屏幕的距离：总距离）* 音量最大值
                float voiceChange = (distanceY / touchRang) * maxVoice;
                //最终音量 = 原来的音量 + 改变的音量
                int voice = (int) Math.min(Math.max(mVol + voiceChange,0),maxVoice);
                if(voiceChange != 0){//声音发生了改变
                    isMute = false;
                    setVoiceState(voice,isMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController(){
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController(){
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;
    }

    /**
     * 监听物理键
     * 设置按手机物理键增加和减少音量
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){//减少音量
            currentVoice--;
            setVoiceState(currentVoice,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){//增加音量
            currentVoice++;
            setVoiceState(currentVoice,false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER,5000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
