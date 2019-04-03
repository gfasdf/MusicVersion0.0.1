package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.example.myapplication.pojo.Music;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private boolean isInitialState = true;
    /**
     *定时任务
     */
    private Timer timer;
    /**
     * 音乐列表
     */
    private List<Music> musicList;



    /**
     * mediaPlayer：音频播放器
     */
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //当前播放音乐的下标，默认是0
    private int currentSub = 0;
    @Override
    public IBinder onBind(Intent intent) {
        this.musicList = (List<Music>)intent.getSerializableExtra("musicList");
        //返回MusicBinder对象用以实现对服务的控制
        sendMessage();//开始循环发送广播
        initCompone();
        /**
         * 开启用户检测
         */
        return new MusicBinder();
    }

    /**
     * 初始化组件(自动播放下一曲)
     */
    public void initCompone(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    currentSub++;
                    if(currentSub >= musicList.size()){
                        currentSub = 0;
                    }
                    mediaPlayer.reset();
                    //调用方法传进去要播放的音频路
                    mediaPlayer.setDataSource(musicList.get(currentSub).getUrl());
                    //异步准备音频资源
                    mediaPlayer.prepareAsync();
                    //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();//开始音频

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 判断当前是否在播放音乐
     * @return
     */
    public boolean isPlayingMusic(){
        if(mediaPlayer == null){
            return false;
        }else{
            return mediaPlayer.isPlaying();
        }

    }
    /**
     * 内部类，用以控制
     */
    public class MusicBinder extends Binder {




        public MusicBinder() {

        }
        /**
         * 播放上一曲
         */
        public void pre(){
            currentSub--;
            /**
             * 如果是第一首
             */
            if(currentSub < 0){
                currentSub = musicList.size() - 1;
            }
            palyMusicByPositionStartOver(currentSub);

        }
        /**
         * 播放下一曲
         */
        public void next(){
            currentSub++;
            if(currentSub >= musicList.size()) {
                currentSub = 0;
            }
            palyMusicByPositionStartOver(currentSub);

        }

        /**
         * 根据下标播放音乐
         * @param position
         */
        public void palyMusicByPositionStartOver(int position){

            try {

                mediaPlayer.reset();
                //调用方法传进去要播放的音频路径
                mediaPlayer.setDataSource(musicList.get(position).getUrl());
                isInitialState = false;
                //异步准备音频资源
                mediaPlayer.prepareAsync();
                //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();//开始音频

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 暂停或者继续:
         * 当播放转为暂停：返回true
         * 当暂停转为播放：返回false
         *
         */
        public void pauseOrProcess(){
            if (isPlayingMusic()) {
                mediaPlayer.pause();
            } else {
                if(isInitialState){
                    palyMusicByPositionStartOver(currentSub);

                }else{
                    mediaPlayer.start();
                }
            }
        }

        /**
         * 点击列表中音乐来播放
         */
        public void clickPlay(int position){
            currentSub = position;
            palyMusicByPositionStartOver(currentSub);

        }
    }



    //定时任务发送广播到主界面
    public void sendMessage() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    /**
                     * 发送广播：
                     *      内容包括：歌曲名
                     *               歌曲作者+ 专辑
                     *
                     */
                    Intent intent = new Intent();
                    intent.setAction("MusicInfo");
                    /**
                     * 将当前播放的音乐的下标传递到主界面
                     */
                    intent.putExtra("currentSub", currentSub);
                    intent.putExtra("isPlay",isPlayingMusic());


                    sendBroadcast(intent);
                }
                //开始计时任务后的5毫秒，第一次执行run方法，以后每500毫秒执行一次
            }, 5, 500);
        }
    }


}
