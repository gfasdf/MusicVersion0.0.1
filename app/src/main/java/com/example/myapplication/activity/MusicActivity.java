package com.example.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MusicListAdapter;
import com.example.myapplication.pojo.Music;
import com.example.myapplication.service.MusicService;
import com.example.myapplication.utils.MusicUtils;

import java.io.Serializable;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ImageView pre;
    private ImageView next;
    private ImageView pauseOrProcess;
    private ImageView imgMusic;
    private TextView musicTitle;
    private TextView musicArtist;
    private ListView listMusic;
    private List<Music> musicList;
    private MusicService.MusicBinder musicBinder;
    private MusicServiceConnection connection;
    private MusicBroadcastReceiver receiver;//广播接收者
    /**
     * Activity销毁时解除广播接收者绑定
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initComponents();
        MusicUtils.refresh(this);
        loadMusicList();
        /**
         * 开启服务
         */
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("musicList", (Serializable)musicList);
        startService(intent);
        connection = new MusicServiceConnection();
        /**
         * 绑定服务
         */
        bindService(intent,connection, BIND_AUTO_CREATE);
        /**
         * 广播接收者
         */
        receiver = new MusicBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("MusicInfo");
        registerReceiver(receiver,intentFilter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(receiver);
    }


    private void initComponents() {

        listMusic = (ListView) findViewById(R.id.lv_musics);
        listMusic.setOnItemClickListener(this);

        pre = (ImageView) findViewById(R.id.imgPre);
        next = (ImageView) findViewById(R.id.imgNext);


        pauseOrProcess = (ImageView) findViewById(R.id.imgPauseOrProcess);
        imgMusic = (ImageView) findViewById(R.id.imgMusic);
        musicTitle = (TextView) findViewById(R.id.textMusicTitle);
        musicArtist = (TextView) findViewById(R.id.textMusicArtist);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        pauseOrProcess.setOnClickListener(this);
    }

    private void loadMusicList() {
        musicList = MusicUtils.loadMusicList(this);

        BaseAdapter adapter = new MusicListAdapter(this, musicList);
        listMusic.setAdapter(adapter);

    }

    //监听ListView的方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       musicBinder.clickPlay(position);
       pauseOrProcess.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgPre:
                musicBinder.pre();
                //点击了上一首
                break;
            case R.id.imgNext:
                musicBinder.next();
                break;
            case R.id.imgPauseOrProcess://点击了暂停按钮
                musicBinder.pauseOrProcess();
                break;
        }
    }

    /**
     * 服务连接
     */
    private class MusicServiceConnection implements ServiceConnection {

        /**
         * 当连接上服务时
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            Log.i("MusicServiceConnection","服务连接成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }

    /**
     * 广播Receiver
     */
    private class MusicBroadcastReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MusicBroadcastReceiver","接收到了广播");
            /**
             * 取出当前播放音乐的下标
             */
            int currentSub = intent.getIntExtra("currentSub",0);
            boolean isPlayMusic = intent.getBooleanExtra("isPlay",false);
            Music music = musicList.get(currentSub);
            musicTitle.setText(music.getTitle());
            musicArtist.setText(music.getArtist());
            //图片
            imgMusic.setImageBitmap(MusicUtils.getAlbumArt(context,music.getAlbumId()));
            if(isPlayMusic){
                pauseOrProcess.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            }else{
                pauseOrProcess.setImageDrawable(getResources().getDrawable(R.drawable.process));

            }
        }
    }

}
