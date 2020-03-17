package com.example.ygodeckbuilder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;


/**
 * Fragment that displays the decks list and allows to create and delete decks
 */
public class DecksListFragment extends Fragment {

	public static final String EXTRA_CARD_IDS = "com.example.ygodeckbuilder.CARD_IDS";

	private Button addDeck;
	private ListView decksListView;
	private DeckRowAdapter adapter;
	private Context deckListContext;
	private String[] names;

	public DecksListFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_decks_list, container, false);

		deckListContext = view.getContext();

		// set the button to add a deck
		addDeck = view.findViewById(R.id.addDeck);
		addDeck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createDeck();
			}
		});

		// set the listview
		decksListView = view.findViewById(R.id.decksListview);
		names = new String[MainActivity.deckList.size()];
		Set<String> namesSet = MainActivity.deckList.keySet();
		int i = 0;
		for(String name : namesSet)
			names[i++] = name;

		// set the adapter to diplay a custom deck list view with see and delete buttons
		adapter = new DeckRowAdapter(deckListContext, names, MainActivity.deckList);
		decksListView.setAdapter(adapter);
		setClickable(decksListView);

		return view;
	}

	class DeckRowAdapter extends ArrayAdapter<String> {
		Context context;
		HashMap<String, Vector<String>> decksHashMap;
		String[] names;

		DeckRowAdapter(Context c, String[] names, HashMap<String, Vector<String>> decksHashMap) {
			super(c, R.layout.deck_row_delete, R.id.deckName, names);
			this.context = c;
			this.decksHashMap = decksHashMap;
			this.names = names;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View deckRow = layoutInflater.inflate(R.layout.deck_row_delete, parent, false);
			TextView deckNames = deckRow.findViewById(R.id.deckName);
			TextView deckNumCard = deckRow.findViewById(R.id.deckNumCard);
			Button deleteDeckButton = deckRow.findViewById(R.id.deleteDeckButton);
			Button editDeckButton = deckRow.findViewById(R.id.editDeckButton);

			// set the name of the decks and the number of cards
			deckNames.setText(names[position]);
			deckNumCard.setText("Number of cards: " + decksHashMap.get(names[position]).size());

			// set the delete button action
			deleteDeckButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DeleteDeckDialog deleteDeckDialog = new DeleteDeckDialog(names[position]);
					deleteDeckDialog.show(getActivity().getSupportFragmentManager(), "delete deck dialog");
				}
			});

			// set the action to see the deck
			editDeckButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// new activity with the deck clicked
					Intent intent = new Intent(getContext(), DeckCardsActivity.class);

					String[] cardIds = new String[MainActivity.deckList.get(names[position]).size()];
					for(int i = 0; i < cardIds.length; i++)
						cardIds[i] = MainActivity.deckList.get(names[position]).get(i);

					intent.putExtra(EXTRA_CARD_IDS, cardIds);
					startActivity(intent);
				}
			});

			return deckRow;
		}
	}

	private void setClickable(ListView cardsListView) {
		cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// new activity with the deck clicked
				Intent intent = new Intent(view.getContext(), DeckCardsActivity.class);

				String[] cardIds = new String[MainActivity.deckList.get(names[position]).size()];
				for(int i = 0; i < cardIds.length; i++)
					cardIds[i] = MainActivity.deckList.get(names[position]).get(i);

				intent.putExtra(EXTRA_CARD_IDS, cardIds);
				startActivity(intent);
			}
		});
	}

	public void createDeck() {
		CreateDeckDialog createDeckDialog = new CreateDeckDialog();
		createDeckDialog.show(getActivity().getSupportFragmentManager(), "create deck dialog");
	}

}
