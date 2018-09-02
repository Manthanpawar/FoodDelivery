package foodxpress.foodxpress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by manth on 3/5/2018.
 */

class SectionpagerAdapter extends FragmentPagerAdapter {

    public SectionpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                recomanded recmd=new recomanded();
                return  recmd;
            case 1:
                restaurants restrant=new restaurants();
                return restrant;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "RECOMMENDED";
                case 1:
                    return "RESTAURANTS";
                    default:
                        return null;

            }
    }
}
