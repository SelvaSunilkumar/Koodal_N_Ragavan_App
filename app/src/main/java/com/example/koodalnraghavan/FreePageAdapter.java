package com.example.koodalnraghavan;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FreePageAdapter extends FragmentPagerAdapter {

    int numberOfPages;
    FreeCallerTone freeCallerTone;
    TextTab textTab;

    public FreePageAdapter(FragmentManager fm,int numberOfPages)
    {
        super(fm);
        this.numberOfPages = numberOfPages;
        freeCallerTone = new FreeCallerTone();
        textTab = new TextTab();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        //FreeCallerTone freeCallerTone = new FreeCallerTone();
        //TextTab textTab = new TextTab();

        //View view;

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
