package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FreePageAdapter extends FragmentPagerAdapter {

    int numberOfPages;

    public FreePageAdapter(FragmentManager fm,int numberOfPages)
    {
        super(fm);
        this.numberOfPages = numberOfPages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        FreeCallerTone freeCallerTone = new FreeCallerTone();
        TextTab textTab = new TextTab();

        switch (position)
        {
            case 0:
                return freeCallerTone;
            case 1:
                return textTab;
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
