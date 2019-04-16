package zzuli.com.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import zzuli.com.music.R;
import zzuli.com.music.adapter.ViewPageAdapter;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private List<ImageView> pointViews = new ArrayList<>();
    private int currentIndex;
    private int pageSize;//页面的个数
    private ViewPager viewPager;
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        initPoint();
    }

  /*  public void clickEnter(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
    @Override
    public void onClick(View view) {


        ImageView imageView = (ImageView)view;
        int skipNum = (int) imageView.getTag();

        pointViews.get(currentIndex).setEnabled(true);//取消当前选中状态
        currentIndex = skipNum;
        pointViews.get(currentIndex).setEnabled(false);//当前的圆点设置选中

        viewPager.setCurrentItem(skipNum);//跳转
    }






    public void initPoint(){

        //初始化ViewPager
        viewPager = findViewById(R.id.vp_pic);
        viewList = new ArrayList<>();
        viewList.add(View.inflate(this,R.layout.page_one,null));
        viewList.add(View.inflate(this,R.layout.page_two,null));
        viewList.add(View.inflate(this,R.layout.page_three,null));
        viewPager.setAdapter(new ViewPageAdapter(viewList));
        viewPager.addOnPageChangeListener(this);

        /**
         *设置点击事件
        */
        View view = View.inflate(this,R.layout.page_three,null);
   /*     Button btnEnter = view.findViewById(R.id.btn_enter);
        btnEnter.bringToFront();*/
        pageSize = viewList.size();//设置圆点个数

        //初始化小圆点
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll_points);
        linearLayout.bringToFront();//设置布局在最上层显示，避免点击无效

        for(int i = 0;i < pageSize;i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            );

            params.leftMargin = 10;//左间距
            params.rightMargin = 10;//右间距

            imageView.setBackgroundResource(R.drawable.point);
            imageView.setEnabled(true);//设成默认未选中
            imageView.setOnClickListener(this);
            imageView.setTag(i);//标记值
            linearLayout.addView(imageView,params);
            pointViews.add(imageView);//添加到容器中
        }
        currentIndex = 0;//设置当前下标为0
        pointViews.get(currentIndex).setEnabled(false);//初始为被选中*/


    }


    @Override
    public void onPageSelected(int i) {
        pointViews.get(currentIndex).setEnabled(true);//取消当前选中状态
        currentIndex = i;
        //当前的圆点设置选中
        pointViews.get(currentIndex).setEnabled(false);
    }

    @Override
    public void onPageScrollStateChanged(int i) { }

    @Override
    public void onPageScrolled(int position, float v, int i1) {
        Log.i("SplashActivity:", "onPageScrolled: 当前页面被滑动时调用：：：：position值为： " + position);
        if (position == (viewList.size() - 1)) {//滑动到最后一页
            Log.i("SplashActivity:", "onPageScrollStateChanged: 滑动到最后一页::::::");
                Intent intent = new Intent(WelcomeActivity.this, LeftNavigationActivity.class);
            startActivity(intent);
            finish();



        }
    }


}
