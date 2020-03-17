package com.example.ygodeckbuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

	private static String SAVE_DECKS_LIST_FILE;

	public static HashMap<String, Vector<String>> deckList;
	private static MainActivity mainActivity;

	private Toolbar menuToolBar;
	private TabLayout menuTabLayout;
	private ViewPager pager;
	private ViewPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SAVE_DECKS_LIST_FILE = this.getFilesDir().getAbsolutePath() + "/save_decks_list";

		// intialize the deck list
		MainActivity.deckList = new HashMap<>();
		MainActivity.restore();

		// set the toolbar and the viewpager
		menuToolBar = findViewById(R.id.menuToolBar);
		setSupportActionBar(menuToolBar);

		pager = findViewById(R.id.pager);
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);

		menuTabLayout = findViewById(R.id.menuTabLayout);
		menuTabLayout.setupWithViewPager(pager);


		mainActivity = this;
	}

	// reset the viewPageAdapter to recreate the fragments
	public void refresh() {
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		menuTabLayout.setupWithViewPager(pager);
	}

	public static void staticRefresh() {
		mainActivity.refresh();
	}

	// serialize the hashmap with the decks
	public static void backup() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_DECKS_LIST_FILE));
			out.writeObject(deckList);
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// deserialize the hashmap with the decks
	public static void restore() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_DECKS_LIST_FILE));
			MainActivity.deckList = (HashMap<String, Vector<String>>) in.readObject();
			in.close();
		} catch(IOException e) {
			MainActivity.deckList = new HashMap<>();
		} catch(ClassNotFoundException e) {
			MainActivity.deckList = new HashMap<>();
		}
	}
}
