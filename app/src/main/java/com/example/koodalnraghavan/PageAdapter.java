package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    int numberOfPages;

    public PageAdapter(FragmentManager fm,int numberOfPages)
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
                return new Videotab();
            case 1:
                return new Musictab();
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
