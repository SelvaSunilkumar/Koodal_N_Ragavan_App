package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BabyNameAdapter extends FragmentPagerAdapter {

    int NumberOFPages;

    public BabyNameAdapter(FragmentManager fm,int NumberOfPages)
    {
        super(fm);
        this.NumberOFPages = NumberOfPages;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new BoyBabyName();
            case 1:
                return new GirlBabyNames();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NumberOFPages;
    }

    @Override
    public long getItemId(int position) {
        return POSITION_NONE;
    }
}
