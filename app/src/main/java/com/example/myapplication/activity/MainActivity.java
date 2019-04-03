package com.example.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FragmentPageAdapter;
import com.example.myapplication.fragment.FirstFragment;
import com.example.myapplication.fragment.SecondFragment;
import com.example.myapplication.fragment.ThirdFragment;
import com.example.myapplication.message.SendMessageCommunitor;
import com.example.myapplication.pojo.Music;
import com.example.myapplication.service.MusicService;
import com.example.myapplication.utils.MusicUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendMessageCommunitor,View.OnClickListener{
    private ImageView pre;
    private ImageView next;
    private ImageView pauseOrProcess;
    private ImageView imgMusic;
    private TextView musicTitle;
    private TextView musicArtist;
    private List<Music> musicList;
    private MusicService.MusicBinder musicBinder;
    private MainActivity.MusicServiceConnection connection;
    private MainActivity.MusicBroadcastReceiver receiver;//广播接收者

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        initComponents();
        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(receiver);
    }

    //自动加载菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if(menuItemId == R.id.menu_search){
            Toast.makeText(MainActivity.this,"menu_search",Toast.LENGTH_SHORT).show();
        }else if(menuItemId == R.id.menu_option){
            Toast.makeText(MainActivity.this,"menu_option",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * 初始化Fragment组件
     */
    public void initFragments(){
        //find Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_title);
        toolbar.setTitle("音乐播放器");
        //设置兼容性
        setSupportActionBar(toolbar);
        //设置是否有返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
        List<String> tabTitles = new ArrayList<>();
        tabTitles.add("本地音乐");
        tabTitles.add("网络音乐");
        tabTitles.add("个人中心");
        ViewPager viewPager = findViewById(R.id.vp_list);
        List<Fragment> fragments = new ArrayList<>();
        MusicUtils.refresh(this);
        musicList = MusicUtils.loadMusicList(this);
        /*fragments.add(new SecondFragment());*/
        fragments.add(FirstFragment.newInstance(musicList));
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());
        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),fragments,tabTitles));
        TabLayout tabLayout = findViewById(R.id.tl_menu);
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * 初始化控件
     */
    private void initComponents() {
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

    /**
     * 开启服务
     */
    public void startService(){
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
    public void sendMessage(int position) {
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
            Log.i("MusicServiceConnection","服务取消连接成功");
        }

    }

    /**
     * 广播Receiver
     */
    private class MusicBroadcastReceiver extends BroadcastReceiver {


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
