package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class jodhidampageadapter extends FragmentPagerAdapter {

    int numberOfPages;

    public jodhidampageadapter(FragmentManager fm,int numberOfPages)
    {
        super(fm);
        this.numberOfPages = numberOfPages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new jodhidamvideotab();
            case 1:
                return new jodhidammusictab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
