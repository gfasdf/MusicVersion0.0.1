package zzuli.com.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import zzuli.com.music.R;
import zzuli.com.music.activity.LeftNavigationActivity;
import zzuli.com.music.adapter.RecyclerViewAdapter;
import zzuli.com.music.message.SendMessageCommunitor;
import zzuli.com.music.pojo.Music;
import zzuli.com.music.utils.MusicUtils;

public class LocalMusicFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
/*    @BindView(R.id.local_RV)
    RecyclerView localRV;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;*/
    Unbinder unbinder;
    private Context context;
    private List<Music> musicList;
    private SendMessageCommunitor sendMessage;
    RecyclerViewAdapter mRecyclerViewAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    public LocalMusicFragment() {
    }

    public static LocalMusicFragment newInstance(List<Music> musicList) {
        Bundle args = new Bundle();
        LocalMusicFragment fragment = new LocalMusicFragment();
        args.putSerializable("musicList", (Serializable) musicList);//ArrayList实现了Serializable接口
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

    private void initView() {

        //初始化下拉刷新
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(this);

        //初始化RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.i("LocalMusicFragment:", "onCreateView: 渲染fragment_local_music：：：：：：");
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        recyclerView = view.findViewById(R.id.local_RV);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        mRecyclerViewAdapter = new RecyclerViewAdapter(context, musicList,1);//本地音乐适配器加载
        recyclerView.setAdapter(mRecyclerViewAdapter);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendMessage.sendMessage(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendMessage = (SendMessageCommunitor) context;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (musicList != null) {
            musicList.clear();
        }
        musicList = (List<Music>) MusicUtils.loadMusicList(LocalMusicFragment.this.getContext());
        mRecyclerViewAdapter.flushList(musicList);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
