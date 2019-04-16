package zzuli.com.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzuli.com.music.R;
import zzuli.com.music.listener.OnRecyclerviewItemClickListener;
import zzuli.com.music.pojo.Music;
import zzuli.com.music.utils.MusicUtils;

/**
 * @author qichaoqun
 * @date 2019/1/19
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener {


    private Context mContext = null;
    private List<Music> mMusicList = null;
    private int type;
    private OnRecyclerviewItemClickListener mOnItemClickListener = null;
    private Bitmap bitmap;

    /**
     *
     * @param context
     * @param mMusicList
     * @param type:传进来的音乐来源类型:本地音乐、网络音乐
     */
    public RecyclerViewAdapter(Context context, List<Music> mMusicList, int type) {
        Log.i("RecyclerViewAdapter::", "RecyclerViewAdapter: 适配器获取的音乐列表为：：：：：" + mMusicList);
        mContext = context;
        this.mMusicList = mMusicList;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,
                viewGroup, false);
//        Log.i("RecyclerViewAdapter:", "onCreateViewHolder: 渲染本地音乐的每一项：：：：：：");
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Music music = mMusicList.get(i);
        myViewHolder.tvTitle.setText(music.getTitle());
        if(type == 1){//音乐类型为本地音乐
            Bitmap musicImg = MusicUtils.getAlbumArt(mContext,music.getAlbumId());
            myViewHolder.ivMusic.setImageBitmap(musicImg);
        }else if(type == 2){//音乐类型为网络音乐
            returnBitMap(music.getAlbumId(),myViewHolder.ivMusic);
            Glide.with(mContext).load(music.getAlbumId()).into(myViewHolder.ivMusic);
        }

        myViewHolder.tvTime.setText(MusicUtils.getTime(music.getDuration()));
        myViewHolder.tvArtist.setText(music.getArtist() + "-" +music.getAlbum());
        myViewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
//        Log.i("RecyclerViewAdapter:", "getItemCount: 获取了 " + mMusicList.size() + " 项数据==========");
        return mMusicList == null ? 0 : mMusicList.size();
    }

    @Override
    public void onClick(View v) {
//        Log.i("RecyclerViewAdapter:", "onClick: 点击了RecyclerView,设置onItemClickListener：：：：：");
        mOnItemClickListener.onItemClickListener(v, ((int) v.getTag()));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_Music)
        ImageView ivMusic;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_Artist)
        TextView tvArtist;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 点击事件的回调方法
     *
     * @param itemClickListener 接口对象，用来实现的
     */
    public void setOnItemClickListener(OnRecyclerviewItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public void flushList(List<Music> list) {
        mMusicList = list;
        notifyDataSetChanged();
    }

    public void returnBitMap(final String url, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    //conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                    //is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
