package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.pojo.Music;
import com.example.myapplication.utils.MusicUtils;

import java.util.List;

public class MusicListAdapter  extends BaseAdapter {


    private Context context;
    private List<Music> itemsList;

    public MusicListAdapter(Context context,List<Music> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Music music = itemsList.get(position);
        View view = View.inflate(context,R.layout.item_layout,null);
        //标题
        TextView textMusicTitle = (TextView)view.findViewById(R.id.textMusicTitle);
        //作者及专辑
        TextView textArtist = (TextView)view.findViewById(R.id.textMusicArtist);
        //时长
        TextView textTime = (TextView)view.findViewById(R.id.textTime);
        //专辑图片
        ImageView imageView = (ImageView)view.findViewById(R.id.imageMusic);

        //标题
        textMusicTitle.setText(music.getTitle());
        Bitmap musicImg = MusicUtils.getAlbumArt(context,music.getAlbumId());
        //图片
        imageView.setImageBitmap(musicImg);

        //时长
        textTime.setText(MusicUtils.getTime(music.getDuration()));
        //歌手和专辑
        textArtist.setText(music.getArtist() + "-" +music.getAlbum());
        return view;
    }
}
