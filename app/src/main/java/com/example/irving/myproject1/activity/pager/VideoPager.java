package com.example.irving.myproject1.activity.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.irving.myproject1.R;
import com.example.irving.myproject1.activity.SystemMediaPlayer;
import com.example.irving.myproject1.activity.base.BasePager;
import com.example.irving.myproject1.activity.domain.MediaItem;
import com.example.irving.myproject1.activity.utils.Utils;

import java.util.ArrayList;

import static com.example.irving.myproject1.R.id.video_icon;

/**
 * Created by Irving on 2017/10/24.
 * 本地视频页面
 */

public class VideoPager extends BasePager{
    public static final String TAG = VideoPager.class.getSimpleName();
    public VideoPager(Context context) {
        super(context);
    }

    private ArrayList<MediaItem> mediaItems;
    private ListView listView;
    private TextView textView;
    private ProgressBar progressBar;
    private MediaItem mediaItem;
    private Utils utils;

    private ListAdapter VideoAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() > 0){
                //有数据，显示数据，隐藏文本
                textView.setVisibility(View.GONE);
                VideoAdapter = new VideoAdapter();
                listView.setAdapter(VideoAdapter);
            }else{
                //没有数据，显示文本
                textView.setVisibility(View.VISIBLE);
            }
            //隐藏progressBar
            progressBar.setVisibility(View.GONE);
        }
    };

    //实现本地视频页面的初始化
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager,null);
        listView = (ListView) view.findViewById(R.id.listView);
        textView = (TextView) view.findViewById(R.id.textView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //设置ListView的Item的点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    //实现本地视频页面的数据的
    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "initData: "+ "本地音频页面被初始化了" );
        //从本地内存中获取视频数据
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        /**
         * 从本地的sdcard中获取视频
         * 1.遍历sdcard，后缀名
         * 2.从内容提供者中获得(推荐)
         */

        //创建一个子线程
        new Thread() {
            @Override
            public void run() {
                super.run();

                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] video = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频的名称
                        MediaStore.Video.Media.DURATION,//视频的总时长
                        MediaStore.Video.Media.SIZE,//视频的大小
                        MediaStore.Video.Media.ARTIST,//作者
                        MediaStore.Video.Media.DATA//视频的绝对路径
                };
                Cursor cursor = contentResolver.query(uri,video,null,null,null);
                mediaItems = new ArrayList<>();
                if(contentResolver != null){
                    while(cursor.moveToNext()){
                        mediaItem = new MediaItem();
                        mediaItems.add(mediaItem);

                        String name = cursor.getString(0);
                        mediaItem.setName(name);
                        Long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        Long size = cursor.getLong(2);
                        mediaItem.setSize(size);
                        String artist = cursor.getString(3);
                        mediaItem.setArtist(artist);
                        String data = cursor.getString(4);
                        mediaItem.setData(data);
                    }
                    cursor.close();
                }
                //发送一个消息给主线程
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    class VideoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.listview_vidio_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.video_icon = (ImageView) convertView.findViewById(video_icon);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

                convertView.setTag(viewHolder);
            }else{
               viewHolder = (ViewHolder) convertView.getTag();
            }
            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.tv_name.setText(mediaItem.getName());
            viewHolder.tv_size.setText(android.text.format.Formatter.formatFileSize(context,mediaItem.getSize()));
            utils = new Utils();
            viewHolder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
            return convertView;
        }

    }

    static class ViewHolder{
      private ImageView video_icon;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_size;
    }

     class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mediaItem = mediaItems.get(position);

//            Toast.makeText(context, mediaItem.getName()+"-Video", Toast.LENGTH_SHORT).show();

            //1.发出意图，调用系统所有的播放器-隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);

            //2.自定义播放器-显示意图
//            Intent intent = new Intent(context,SystemMediaPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);

            //3.传递播放列表
            Intent intent = new Intent(context,SystemMediaPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");//Uri.parse(mediaItem.getData())将数据转换为uri
            Bundle bundle = new Bundle();
            bundle.putSerializable("vidiolist",mediaItems);
            bundle.putInt("videoposition",position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
