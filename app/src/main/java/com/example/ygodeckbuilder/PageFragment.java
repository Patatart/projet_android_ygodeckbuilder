package com.example.ygodeckbuilder;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Default Blank fragment for the main activity
 */
public class PageFragment extends Fragment {


	TextView textView;

	public PageFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_page, container, false);
		textView = view.findViewById(R.id.messageTest);
		textView.setText(getArguments().getString("message"));

		return view;
	}

}
