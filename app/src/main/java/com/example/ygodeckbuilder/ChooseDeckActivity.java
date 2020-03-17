package com.example.ygodeckbuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * Displays the list of the decks so the user can choose in which one he want to add the card in
 */
public class ChooseDeckActivity extends AppCompatActivity {

	private Context deckListContext;
	private ListView decksListView;

	private  String cardId;

	private String[] names;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_deck);

		deckListContext = this;

		// get the card id
		Intent intent = getIntent();
		cardId = intent.getStringExtra(CardActivity.EXTRA_CARD_ID);

		// set the listview
		decksListView = findViewById(R.id.chooseDeckListView);
		names = new String[MainActivity.deckList.size()];
		Set<String> namesSet = MainActivity.deckList.keySet();
		int i = 0;
		for(String name : namesSet)
			names[i++] = name;

		// set the adapter to diplay a custom deck list view
		DeckRowAdapter adapter = new DeckRowAdapter(deckListContext, names, MainActivity.deckList);
		decksListView.setAdapter(adapter);

		setClickable(decksListView);
	}

	// adapter for the deck list view
	class DeckRowAdapter extends ArrayAdapter<String> {
		Context context;
		HashMap<String, Vector<String>> decksHashMap;
		String[] names;

		DeckRowAdapter(Context c, String[] names, HashMap<String, Vector<String>> decksHashMap) {
			super(c, R.layout.deck_row, R.id.deckName, names);
			this.context = c;
			this.decksHashMap = decksHashMap;
			this.names = names;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View deckRow = layoutInflater.inflate(R.layout.deck_row, parent, false);
			TextView deckNames = deckRow.findViewById(R.id.deckName);
			TextView deckNumCard = deckRow.findViewById(R.id.deckNumCard);

			// set the name of the deck and the number of cards
			deckNames.setText(names[position]);
			deckNumCard.setText("Number of cards: " + decksHashMap.get(names[position]).size());

			return deckRow;
		}
	}

	// set the action to execute when you click on a deck
	private void setClickable(ListView cardsListView) {
		cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// add the cart to the deck, save the list, display message and close the activity
				MainActivity.deckList.get(names[position]).add(cardId);
				MainActivity.backup();
				MainActivity.staticRefresh();
				Toast.makeText(deckListContext, "Card added to the deck", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
