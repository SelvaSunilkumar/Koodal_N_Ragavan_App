package tce.education.koodalnraghavan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OtherAdapter extends FragmentPagerAdapter {

    int number;

    public OtherAdapter(FragmentManager fm,int number)
    {
        super(fm);
        this.number = number;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new OtherAudio();
            case 1:
                return new OtherVideo();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
