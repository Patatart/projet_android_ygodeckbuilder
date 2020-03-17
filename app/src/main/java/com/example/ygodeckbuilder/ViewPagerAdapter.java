package com.example.ygodeckbuilder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Fragment Pager Adapter that returns the fragments for the main activity
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	// Returns a different fragment depending on the position in the toolbar
	@Override
	public Fragment getItem(int position) {
		PageFragment blankFragment = new PageFragment();
		Bundle bundle = new Bundle();
		bundle.putString("message", "Blank Page " + (position + 1));
		blankFragment.setArguments(bundle);

		// set the message for the About page
		String about = "Yu-Gi-Oh! Deck Builder\nProgrammed by Théo Abdmeziem\n\nAPI used : YGOPRODECK\nhttps://db.ygoprodeck.com/api-guide/";

		PageFragment aboutFragment = new PageFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putString("message", about);
		aboutFragment.setArguments(bundle2);

		switch(++position) {
			case 1: return new SearchFragment();
			case 2: return new DecksListFragment();
			case 3: return aboutFragment;
			default: return blankFragment;
		}
	}

	// The number of pages and toolbar tabs
	@Override
	public int getCount() {
		return 3;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		String title;

		switch(position) {
			case 0: title = "Search"; break;
			case 1: title = "My Decks"; break;
			case 2: title = "About"; break;
			default: title = "Menu";
		}
		return title;
	}
}
