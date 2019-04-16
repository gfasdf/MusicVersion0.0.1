package zzuli.com.music.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import zzuli.com.music.R;
import zzuli.com.music.adapter.FragmentPageAdapter;
import zzuli.com.music.adapter.RecyclerViewAdapter;
import zzuli.com.music.app.AppUtil;
import zzuli.com.music.fragment.LocalMusicFragment;
import zzuli.com.music.fragment.MuiseFragment;
import zzuli.com.music.fragment.MyMusicFragment;
import zzuli.com.music.message.SendMessageCommunitor;
import zzuli.com.music.pojo.Music;
import zzuli.com.music.utils.MusicUtils;

public class LeftNavigationActivity extends AppCompatActivity
                                    implements NavigationView.OnNavigationItemSelectedListener, SendMessageCommunitor {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ListView mListView;
    private List<Fragment> mFragmentList;
    private List<String> tabTitles;
    private List<Music> mMusicList;//本地歌曲列表
    private List<Music> mMuiseList;
    private static final int ERROR = 0;
    private static final int CHANG_UI = 1;
    RecyclerViewAdapter mRecyclerViewAdapter;
    private String muisePath = "http://tingapi.ting.baidu.com/v1/restserver/ting?for" +
            "mat=json&calback=&from=webapp_music&method=baidu.ting.billboard.billList&type=1&size=10&offset=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LeftNavigationActivity：", "onCreate: 打开activity_left_navigation：：");
        setContentView(R.layout.activity_left_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("音乐播放器");
        setSupportActionBar(toolbar);

        //红色信封点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //LeftNavigationActivity对应的整个界面添加抽屉（侧滑）事件
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //左侧菜单项的点击监听事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //初始化tablelayout和对应fragment
        initView();
    }
    //初始化tablelayout和对应fragment
    private void initView() {
        Log.i("LeftNavigationActivity：", "initView： 初始化view：：：");
        //初始化tablayout
        tabTitles = new ArrayList<>();
        tabTitles.add("本地音乐");
        tabTitles.add("网络曲库");
        tabTitles.add("我的");
        mTabLayout = findViewById(R.id.myTableLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(2)));
        //初始化viewpager
        Log.i("LeftNavigationActivity：", "initView： 初始化viewPager：：：");
        mViewPager = findViewById(R.id.myViewPager);
        mFragmentList = new ArrayList<>();
        mMusicList = MusicUtils.loadMusicList(LeftNavigationActivity.this);
//        Log.i("LeftNavigationActivity:", "initView: 本地音乐列表第一首歌曲为：：：：" + mMusicList.get(0).toString());
        mFragmentList.add(LocalMusicFragment.newInstance(mMusicList));
        loadMuiseHttpUrlConnection(muisePath);
        //        Log.i("LeftNavigationActivity:", "initView: 获取的getApp值为：：：：" + getApp().getMusicList().toString());
        mFragmentList.add(MuiseFragment.newInstance());
        mFragmentList.add(new MyMusicFragment());
        mViewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),mFragmentList,tabTitles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.left_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendMessage(int position) {
        System.out.println("点击了下标为"+ position + "的歌曲");
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CHANG_UI) {
                Log.i("LeftNavigation:", "handleMessage: handler事件发生。。。。。。。。");
                mMuiseList = (List<Music>) msg.obj;
                mRecyclerViewAdapter = new RecyclerViewAdapter(LeftNavigationActivity.this, (List<Music>) msg.obj, 2);
            } else if (msg.what == ERROR) {
                Toast.makeText(LeftNavigationActivity.this, "显示错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 加载网络歌曲信息
     */
    public void loadMuiseHttpUrlConnection(final String loadPath) {
        Log.i("LeftNavigation:", "loadMuiseHttpUrlConnection: 开始加载网络歌曲信息：：：：：：：：：");
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn;
                try {
                    mMuiseList = new ArrayList<>();
                    Log.i("LeftNavigation:", "run: 创建一个mMuiseList对象：：：：");
                    URL url = new URL(loadPath);
                    Log.i("LeftNavigation:", "run: url：：：："+ loadPath);
                    conn = (HttpURLConnection) url.openConnection();
                    Log.i("LeftNavigation:", "run: conn = (HttpURLConnection) url.openConnection():::::");
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    Log.i("LeftNavigation:", "run: 返回码为：：：：：：" + code);
                    if (code == 200) {
                        Log.i("LeftNavigation:", "run: 返回码为200：：：：：：");
                        InputStream is = conn.getInputStream();
                        String json = MusicUtils.getStreamString(is);
                        mMuiseList = MusicUtils.parseJson(json);
                        Log.i("LeftNavigation:", "run: 获取歌曲列表长度：：：：：" + mMuiseList.size());
                        Log.i("LeftNavigation:", "run: mMuiseList第一个值为：： " + mMuiseList.get(0).toString());
                        getApp().setMusicList(mMuiseList);
                        Log.i("LeftNavigation:", "run: getApp().setMusicList(mMuiseList)值为：： " + getApp().getMusicList().get(0));
                        Message message = new Message();
                        message.what = CHANG_UI;
                        message.obj = mMuiseList;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = ERROR;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public AppUtil getApp() {
        return ((AppUtil) getApplicationContext());
    }
}
