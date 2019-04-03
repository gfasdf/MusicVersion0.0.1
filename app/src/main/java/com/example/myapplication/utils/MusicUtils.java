package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.myapplication.R;
import com.example.myapplication.pojo.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    //毫秒转秒
    public static String getTime(long duration) {
        long secondNum = duration / 1000;
        if(secondNum == 0){
            return duration + "ms";
        }else{
            long minNum = secondNum / 60;
            secondNum %= 60;
            if(minNum == 0){
                return secondNum + "s";
            }else{
                return minNum + "m" + secondNum + "s";
            }
        }
    }

    /**
     * 根据专辑ID获取专辑封面图
     * @param album_id 专辑ID
     * @return
     */
    public static Bitmap getAlbumArt(Context context,int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" +
                Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {//有图片
            bm = BitmapFactory.decodeFile(album_art);
        } else {//没有图片，使用默认的
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_music);
        }
        return bm;
    }

    //刷新歌曲库

    /**
     *
     * @param context 上下文对象
     */
    public static void refresh(Context context){

        //1.获得文件夹
        File file = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        //2.获取所有文件
        String[] files = file.list();
        if(files != null){
            for (int i = 0; i < files.length; i++) {
                files[i] = file.getAbsolutePath() + File.separator + files[i];
            }
            MediaScannerConnection.scanFile(context, files, null, null);

        }
    }

    /**
     * 加载歌曲信息
     */
    public static List<Music> loadMusicList(Context context) {


        List<Music> musicList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        String[] columns = new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            Music music = new Music(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getLong(4),
                    cursor.getInt(5),
                    cursor.getString(6));
            musicList.add(music);
            while (cursor.moveToNext()) {
                music = new Music(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getLong(4),
                        cursor.getInt(5),
                        cursor.getString(6));
                musicList.add(music);
            }
        }

        return musicList;

    }
}
