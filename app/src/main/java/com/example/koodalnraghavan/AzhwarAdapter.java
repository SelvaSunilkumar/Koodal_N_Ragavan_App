package com.example.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AzhwarAdapter extends FragmentPagerAdapter {
    int numberOfPages;
    public AzhwarAdapter(FragmentManager fm,int numberOfPages){
        super(fm);
        this.numberOfPages=numberOfPages;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new Azhwarmusictab();
            case 1:
                return new Azhwarvideotab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

    @Override
    public long getItemId(int position) {
        return POSITION_NONE;
    }
}
