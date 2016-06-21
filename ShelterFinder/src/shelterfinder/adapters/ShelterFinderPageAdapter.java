package shelterfinder.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ShelterFinderPageAdapter extends FragmentPagerAdapter {
	private List<Fragment> listFragments;
	public ShelterFinderPageAdapter(FragmentManager fm, List<Fragment> listFragments) {
		super(fm);
		this.listFragments = listFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return listFragments.get(position);
	}

	@Override
	public int getCount() {
		return listFragments.size();
	}

}
