package app.dev.googlesearchapp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import app.dev.googlesearchapp.view.fragment.FavouriteFragment;
import app.dev.googlesearchapp.view.fragment.ResultFragment;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    private Map<Integer, Fragment> registeredFragments = new HashMap<>();

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ResultFragment.newInstance();
            case 1:
                return FavouriteFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    private Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void refreshTab(int position, String searchQuery) {
        switch (position) {
            case 0:
                ResultFragment rf = (ResultFragment) getRegisteredFragment(position);
                rf.update(searchQuery);
                break;
            case 1:
                FavouriteFragment ff = (FavouriteFragment) getRegisteredFragment(position);
                ff.update(searchQuery);
                break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "поиск";
            case 1:
                return "избранное";
            default:
                return "default";
        }
    }
}
