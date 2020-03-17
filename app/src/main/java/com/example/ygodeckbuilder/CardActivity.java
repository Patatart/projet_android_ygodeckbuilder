package com.example.ygodeckbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CardActivity extends AppCompatActivity {

	public static final String EXTRA_CARD_ID = "com.example.ygodeckbuilder.CARD_ID";

	private ImageView cardImageView;
	private Button backButton;
	private Button addCardButton;

	private String cardImageUrl;
	private String cardId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		cardImageView = findViewById(R.id.cardImage);

		// set the back button action
		backButton = findViewById(R.id.cardBackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// get the card data
		Intent intent = getIntent();
		String[] temp = intent.getStringArrayExtra(SearchResultActivity.EXTRA_DISPLAY_CARD);
		cardImageUrl = temp[0];
		cardId = temp[1];

		// download and display the card
		DownloadImageTask downloadImage = new DownloadImageTask();
		downloadImage.execute();

		// set the add button action
		addCardButton = findViewById(R.id.addCardButton);
		addCardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ChooseDeckActivity.class);
				intent.putExtra(EXTRA_CARD_ID, cardId);
				startActivity(intent);
			}
		});
	}

	// AsyncTask to download and display the bitmap of the card
	private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... voids) {
			Bitmap bitmap = null;
			try {
				InputStream inputStream = new URL(cardImageUrl).openStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			} catch(IOException e) {
				e.printStackTrace();
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap cardImage) {
			cardImageView.setImageBitmap(cardImage);
		}
	}
}
