package zzuli.com.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import zzuli.com.music.R;
import zzuli.com.music.pojo.Music;

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
                return minNum + " : " + secondNum;
            }
        }
    }

    /**
     * 根据专辑ID获取专辑封面图
     * @param album_id 专辑ID
     * @return
     */
    public static Bitmap getAlbumArt(Context context,String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" +
                album_id), projection, null, null, null);
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


    //输入流转字符串
    public static String getStreamString(InputStream tInputStream){
        if (tInputStream != null){
            try{
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
                StringBuffer tStringBuffer = new StringBuffer();

                String sTempOneLine = null;
                while ((sTempOneLine = tBufferedReader.readLine()) != null){
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }


    //转换JSON字符串
    public static List<Music> parseJson(String json){
        List<Music> muiseList = new ArrayList<Music>();
        Music muise = null;
        try{
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("song_list");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                muise = new Music();
                muise.setTitle(jsonObject.getString("title"));
                muise.setArtist(jsonObject.getString("author"));
                muise.setDuration(jsonObject.getLong("file_duration"));
                muise.setAlbumId(jsonObject.getString("album_1000_1000"));//专辑图
                muiseList.add(muise);
            }
            return muiseList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 加载歌曲信息
     */
    public static List<Music> loadMusicList(Context context) {
        List<Music> musicList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        String[] columns = new String[]{
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
            Music music = new Music(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getString(5));
            musicList.add(music);
            while (cursor.moveToNext()) {
                music = new Music(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getString(5));
                musicList.add(music);
            }
        }
        return musicList;
    }




}
