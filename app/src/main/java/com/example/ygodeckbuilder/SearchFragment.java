package com.example.ygodeckbuilder;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Fragment to search a card in the main activity
 */
public class SearchFragment extends Fragment {
	public static final String EXTRA_SEARCH_REQUEST = "com.example.ygodeckbuilder.SEARCH_REQUEST";

	private EditText cardnameField;
	private Spinner cardtypeSpinner;
	private Button searchButton;

	public SearchFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_search, container, false);

		// get the search fields
		// card name
		cardnameField = view.findViewById(R.id.cardnameField);

		// type
		cardtypeSpinner = view.findViewById(R.id.cardtypeSpinner);
		ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.types_array, R.layout.spinner_item);
		//typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cardtypeSpinner.setAdapter(typeSpinnerAdapter);

		// set search button action
		searchButton = view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// card name
				String cardname = cardnameField.getText().toString();
				// type
				int cardtypeIndex = cardtypeSpinner.getSelectedItemPosition();
				String cardtype;
				switch(cardtypeIndex) {
					case 2: cardtype = "&type=spell card"; break;
					case 3: cardtype = "&type=trap card"; break;
					case 4: cardtype = "&type=effect monster"; break;
					case 5: cardtype = "&type=normal monster"; break;
					case 6: cardtype = "&type=toon monster"; break;
					case 7: cardtype = "&type=ritual monster"; break;
					case 8: cardtype = "&type=ritual effect monster"; break;
					case 9: cardtype = "&type=fusion monster"; break;
					default: cardtype = "";
				}

				// if all fields are empty
				TextView error = view.findViewById(R.id.searchError);
				if(cardname.equals("") && cardtype.equals("")) {
					error.setText("At least one field must be filled");
				}
				else {
					error.setText("");
					// new activity with the result list
					Intent intent = new Intent(v.getContext(), SearchResultActivity.class);
					intent.putExtra(EXTRA_SEARCH_REQUEST, "https://db.ygoprodeck.com/api/v6/cardinfo.php?fname=" + cardname + cardtype);
					startActivity(intent);
				}
			}
		});

		return view;
	}

}
