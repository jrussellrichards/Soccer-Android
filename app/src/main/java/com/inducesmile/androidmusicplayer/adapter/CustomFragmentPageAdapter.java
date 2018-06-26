package com.inducesmile.androidmusicplayer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inducesmile.androidmusicplayer.fragment.CampeonatosFragment;
import com.inducesmile.androidmusicplayer.fragment.MiCalendarioFragment;
import com.inducesmile.androidmusicplayer.fragment.PartidosFragment;

public class CustomFragmentPageAdapter extends FragmentPagerAdapter{

    private static final String TAG = CustomFragmentPageAdapter.class.getSimpleName();

    private static final int FRAGMENT_COUNT = 3;

    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PartidosFragment();
            case 1:
                return new CampeonatosFragment();
            case 2:
                return new MiCalendarioFragment();
            case 3:
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
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
