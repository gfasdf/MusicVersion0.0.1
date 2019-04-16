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

import javax.security.auth.login.LoginException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import zzuli.com.music.R;
import zzuli.com.music.adapter.RecyclerViewAdapter;
import zzuli.com.music.app.AppUtil;
import zzuli.com.music.message.SendMessageCommunitor;
import zzuli.com.music.pojo.Music;
import zzuli.com.music.utils.MusicUtils;

public class MuiseFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    Unbinder unbinder;
    private Context context;
    private List<Music> mMuiseList;//网络歌曲列表
    private SendMessageCommunitor sendMessage;
    RecyclerViewAdapter mRecyclerViewAdapter;
    RecyclerView muiseRV;
    SwipeRefreshLayout swipeRefresh;

    public MuiseFragment() {
    }

    public static MuiseFragment newInstance() {
//        Log.i("MusicFragment:", "newInstance: 获取的网络音乐列表：：：：" + musicList.get(0).toString());
        Bundle args = new Bundle();
        AppUtil appUtil = new AppUtil();
        Log.i("MuiseFragment::::", "newInstance: MuiseFragment初始化获取的歌曲集合为：：：" + appUtil.getMusicList());
        args.putSerializable("musicList", (Serializable) appUtil.getMusicList());//ArrayList实现了Serializable接口
        MuiseFragment fragment = new MuiseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendMessage.sendMessage(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMuiseList = (List<Music>) getArguments().getSerializable("musicList");
        }
        context = getContext();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("LocalMusicFragment:", "onCreateView: 渲染fragment_Music：：：：：：");
        View view = inflater.inflate(R.layout.fragment_muise, container, false);
        muiseRV = view.findViewById(R.id.muise_RV);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_net);

        mRecyclerViewAdapter = new RecyclerViewAdapter(MuiseFragment.this.getContext(), getApp().getMusicList(),2);
        muiseRV.setAdapter(mRecyclerViewAdapter);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendMessage = (SendMessageCommunitor) context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {

        //初始化下拉刷新
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(this);

        //初始化RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        muiseRV.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onRefresh() {
        if (mMuiseList != null) {
            mMuiseList.clear();
        }
        Log.i("MuiseFragment:", "onRefresh: 刷新获取的歌曲集合为：：：" + getApp().getMusicList());
        mRecyclerViewAdapter.flushList(getApp().getMusicList());
        swipeRefresh.setRefreshing(false);
    }
    public AppUtil getApp() {
        return ((AppUtil) getActivity().getApplicationContext());
    }



}
