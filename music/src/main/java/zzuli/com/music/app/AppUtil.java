package zzuli.com.music.app;

import android.app.Application;

import java.util.List;

import zzuli.com.music.pojo.Music;

public class AppUtil extends Application {
    List<Music> mMusicList;

    public List<Music> getMusicList() {
        return mMusicList;
    }

    public void setMusicList(List<Music> musicList) {
        mMusicList = musicList;
    }
}
