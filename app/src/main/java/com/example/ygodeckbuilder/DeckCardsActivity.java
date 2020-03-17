package com.example.ygodeckbuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Activity that displays all the cards of a deck
 */
public class DeckCardsActivity extends AppCompatActivity {
	ListView cardsListview;
	TextView deckMessage;
	Vector<Card> cards;

	SearchCardTask searchCardTask;
	Vector<String> cardIds;
	Context deckCardsContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deck_cards);
		deckCardsContext = this;

		// get the cards id
		Intent intent = getIntent();
		String[] cardIdsArray = intent.getStringArrayExtra(DecksListFragment.EXTRA_CARD_IDS);
		cardIds = new Vector<>();
		for(int i = 0; i < cardIdsArray.length; i++) {
			cardIds.add(cardIdsArray[i]);
		}

		// get the fields of the layout
		cardsListview = findViewById(R.id.deckCardsListview);
		deckMessage = findViewById(R.id.deckMessage);

		// a vector of Card objects to manage the cards
		cards = new Vector<>();

		// starting the first AsyncTask
		if(searchCardTask != null) {
			searchCardTask.cancel(true);
		}
		searchCardTask = new SearchCardTask();
		searchCardTask.execute();
	}

	// AsyncTask to get the informations of the cards
	private class SearchCardTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... voids) {
			String result = "";
			String temp = "";
			URL url = null;

			// do the requests for each id
			for(String id: cardIds) {
				try {
					url = new URL("https://db.ygoprodeck.com/api/v6/cardinfo.php?id=" + id);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setUseCaches(false);
					urlConnection.connect();
					try {
						InputStream in = new BufferedInputStream(urlConnection.getInputStream());
						temp = readStream(in);
						temp = temp.substring(1, temp.length() - 1);
						result += "," + temp;
					} finally {
						urlConnection.disconnect();
					}
				} catch(MalformedURLException e) {
					e.printStackTrace();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

			// returns a JSON array as a String
			if(result.equals("")) return null;
			result = "[" + result.substring(1) + "]";
			return result;
		}

		// use the JSON array to get cards information and images url
		@Override
		protected void onPostExecute(String result) {
			if(result != null) {
				try {
					Card tempCard;
					JSONArray objectArray = new JSONArray(result);
					JSONArray tempArray;
					JSONObject temp;
					for(int i = 0; i < objectArray.length(); i++) {
						tempCard = new Card();
						temp = objectArray.getJSONObject(i);
						tempArray = temp.getJSONArray("card_images");
						// set card information
						tempCard.setId(temp.getString("id"));
						tempCard.setImageUrlSmall(tempArray.getJSONObject(0).getString("image_url_small"));
						tempCard.setImageUrl(tempArray.getJSONObject(0).getString("image_url"));
						tempCard.setName(temp.getString("name"));
						tempCard.setRace(temp.getString("race"));
						tempCard.setType(temp.getString("type"));
						cards.add(tempCard);
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}

				String[] names = new String[cards.size()];
				int i = 0;
				for(Card card : cards)
					names[i++] = card.getName();
				CardRowAdapter adapter = new CardRowAdapter(deckCardsContext, names, cards, false);
				cardsListview.setAdapter(adapter);

				//setClickable(cardsListview);

				DownloadImagesTask downloadImages = new DownloadImagesTask();
				downloadImages.execute();
			}
			else {
				deckMessage.setGravity(Gravity.CENTER);
				deckMessage.setText("Nothing to display");
			}
		}

	}

	// AsyncTask to download the bitmap of the cards
	private class DownloadImagesTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... voids) {
			Bitmap bitmap;
			for(Card card : cards) {
				bitmap = null;
				try {
					InputStream inputStream = new URL(card.getImageUrlSmall()).openStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					card.setImageSmall(bitmap);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(String s) {
			String[] names = new String[cards.size()];
			int i = 0;
			for(Card card : cards)
				names[i++] = card.getName();

			CardRowAdapter adapter = new CardRowAdapter(deckCardsContext, names, cards, true);
			cardsListview.setAdapter(adapter);

			deckMessage.setText("");
			deckMessage.setHeight(0);
		}
	}

	// Adapter for the list of the cards
	class CardRowAdapter extends ArrayAdapter<String> {
		Context context;
		Vector<Card> cardsResult;
		boolean displayImages;

		CardRowAdapter(Context c, String[] names, Vector<Card> cardsResult, boolean displayImages) {
			super(c, R.layout.card_row, R.id.cardName, names);
			this.context = c;
			this.cardsResult = cardsResult;
			this.displayImages = displayImages;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View cardRow = layoutInflater.inflate(R.layout.card_row, parent, false);
			ImageView cardImages = cardRow.findViewById(R.id.cardImageSmall);
			TextView cardNames = cardRow.findViewById(R.id.cardName);
			TextView cardTypeNAttr = cardRow.findViewById(R.id.cardTypeNAttr);

			// the images are longer to load so we can add the option to not load them
			// the view is updated when the images are loaded
			if(displayImages)
				cardImages.setImageBitmap(cardsResult.get(position).getImageSmall());
			cardNames.setText(cardsResult.get(position).getName());
			cardTypeNAttr.setText(cardsResult.get(position).getRace() + " / " + cardsResult.get(position).getType());

			return cardRow;
		}
	}

	// used to read the result from inputstream to string
	private String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while(i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			return "";
		}
	}

	/*private void setClickable(ListView cardsListView) {
		cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// new activity with the card clicked
				Intent intent = new Intent(getApplicationContext(), CardActivity.class);
				String[] cardData = {cards.get(position).getImageUrl() , cards.get(position).getId()};
				intent.putExtra(EXTRA_DISPLAY_CARD, cardData);
				startActivity(intent);
			}
		});
	}*/

}
