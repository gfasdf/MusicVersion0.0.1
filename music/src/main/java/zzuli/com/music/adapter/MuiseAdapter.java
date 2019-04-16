package zzuli.com.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import zzuli.com.music.R;
import zzuli.com.music.pojo.Music;

public class MuiseAdapter extends BaseAdapter {
    private List<Music> mList;
    private Context context;
    private Bitmap bitmap;

    public MuiseAdapter(List<Music> list, Context context) {
        mList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
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
        View view = convertView.inflate(context,R.layout.list_item, null);
        TextView title = view.findViewById(R.id.tv_title);
        TextView artist = view.findViewById(R.id.tv_Artist);
        TextView duration = view.findViewById(R.id.tv_time);
        ImageView imageView = view.findViewById(R.id.iv_Music);
        returnBitMap(mList.get(position).getUrl(),imageView);
//        Glide.with(context).load(mList.get(position).getUrl()).into(imageView);
        title.setText(mList.get(position).getTitle());
        artist.setText(mList.get(position).getArtist());
        Long dura = mList.get(position).getDuration();
        duration.setText(dura/60 + ":"  + dura % 10);
        String picPath = mList.get(position).getUrl();
        return view;
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
                    if(responseCode == 200){
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
