package com.example.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MusicListAdapter;
import com.example.myapplication.message.SendMessageCommunitor;
import com.example.myapplication.pojo.Music;

import java.io.Serializable;
import java.util.List;


public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener{
    private Context context;
    private List<Music> musicList;
    private SendMessageCommunitor sendMessage;
    public FirstFragment() {

    }

    public static FirstFragment newInstance(List<Music> musicList) {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        args.putSerializable("musicList", (Serializable)musicList);//ArrayList实现了Serializable接口
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            musicList = (List<Music>) getArguments().getSerializable("musicList");
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        //标题
        ListView listView = view.findViewById(R.id.lv_musics);
        BaseAdapter adapter = new MusicListAdapter(context, musicList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendMessage.sendMessage(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendMessage = (SendMessageCommunitor)context;

    }
}
