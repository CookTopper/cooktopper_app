package cooktopper.cooktopperapp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        Fragment fragmentToBeReturned;
        if(position == 0){
            fragmentToBeReturned = new BurnerFragment();
        }
        else{
            fragmentToBeReturned = new ShortcutListFragment();
        }

        return fragmentToBeReturned;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "BOCAS";
            case 1:
                return "ATALHOS";
        }
        return null;
    }
}
