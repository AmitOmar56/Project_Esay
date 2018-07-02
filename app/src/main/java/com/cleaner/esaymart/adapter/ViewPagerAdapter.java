package com.cleaner.esaymart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cleaner.esaymart.fragment.Product_Fragment;
import com.cleaner.esaymart.fragment.Service_Fragment;


/**
 * Created by user on 10/24/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Service_Fragment();
        } else if (position == 1) {
            fragment = new Product_Fragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Service";
        } else if (position == 1) {
            title = "Product";
        }

        return title;
    }
}