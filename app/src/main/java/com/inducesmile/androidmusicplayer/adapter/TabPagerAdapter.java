package com.inducesmile.androidmusicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inducesmile.androidmusicplayer.fragment.PartidosFragment;
import com.inducesmile.androidmusicplayer.fragment.CampeonatosFragment;
import com.inducesmile.androidmusicplayer.fragment.MiCalendarioFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = TabPagerAdapter.class.getSimpleName();

    private static final int FRAGMENT_COUNT = 3;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        //this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new PartidosFragment();
            case 1:
                return new CampeonatosFragment();
            case 2:
                return new MiCalendarioFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Partidos";
            case 1:
                return "Campeonatos";
            case 2:
                return "Mi Calendario";
        }
        return null;
    }
}