package com.saaty.home.StoresProduct;

import com.saaty.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StoreProductsPagerAdaper extends FragmentPagerAdapter {
    public StoreProductsPagerAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new StoreProductFragment("New");
            case 1:
                return new StoreProductFragment("OLd");
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
        case 0:
        return   "new";
        case 1:
        return "old";
        default:
        return null;
    }
    }
}
