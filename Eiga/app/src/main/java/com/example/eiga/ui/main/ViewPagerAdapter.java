package com.example.eiga.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.eiga.R;
import com.example.eiga.ui.favorite.FavoriteNavigation;
import com.example.eiga.ui.list.ListNavigation;
import com.example.eiga.ui.notificationsettings.NotificationSettings;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_list, R.string.tab_fav, R.string.tab_notif};
    private final Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment selectedFragment = new Fragment();
        switch (i) {
            case 0:
                selectedFragment = new ListNavigation();
                break;

            case 1:
                selectedFragment = new FavoriteNavigation();
                break;

            case 2:
                selectedFragment = new NotificationSettings();
                break;
        }

        return selectedFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
