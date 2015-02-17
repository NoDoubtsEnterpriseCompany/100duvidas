package com.nodoubts.core;

import com.nodoubts.ui.fragments.LecturesRegisteredFragment;
import com.nodoubts.ui.fragments.LecturesOferredFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LecturesCreatedAdapter extends FragmentStatePagerAdapter {

	public LecturesCreatedAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new LecturesOferredFragment();
		case 1:
			return new LecturesRegisteredFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}
}
