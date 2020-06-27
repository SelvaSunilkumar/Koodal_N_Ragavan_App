package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PurchaseAdapter extends FragmentPagerAdapter {

    private int number;
    public PurchaseAdapter(@NonNull FragmentManager fm, int behavior,int number) {
        super(fm, behavior);
        this.number = number;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new EbooksPurchase();
            case 1:
                return new AudioPurchase();
            case 2:
                return new VideoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
