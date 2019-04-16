package zzuli.com.music.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> tabTitles;
        public FragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabTitles) {
            super(fm);
            this.tabTitles = tabTitles;
            this.mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList==null?null:mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList==null?0:mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
}