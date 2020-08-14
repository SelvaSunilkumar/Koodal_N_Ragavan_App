package tce.education.koodalnraghavan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tce.education.koodalnraghavan.PrasadhamAudio;
import tce.education.koodalnraghavan.PrasadhamBooks;
import tce.education.koodalnraghavan.PrasadhamVideo;

public class PrasadhamAdapter extends FragmentPagerAdapter {

    private int numberOfPages;

    public PrasadhamAdapter(FragmentManager fm,int behaviour,int numberOfPages)
    {
        super(fm,behaviour);
        this.numberOfPages = numberOfPages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new PrasadhamBooks();
            case 1:
                return new PrasadhamAudio();
            case 2:
                return new PrasadhamVideo();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

}
