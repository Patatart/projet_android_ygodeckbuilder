package com.example.ygodeckbuilder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Vector;

/**
 * Dialog to confirm the supression of a deck
 */
public class DeleteDeckDialog extends AppCompatDialogFragment {

	private String name;

	public DeleteDeckDialog(String name) {
		this.name = name;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_delete_deck, null);

		builder.setView(view).setTitle("Do you want to delete this deck ?")
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// nothing to do just close the dialog
					}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// remove the deck from the hashmap, save, display a message to the user
						MainActivity.deckList.remove(name);
						MainActivity.backup();
						Toast.makeText(getContext(), "Deck deleted", Toast.LENGTH_SHORT).show();
						MainActivity.staticRefresh();
					}
				});

		return builder.create();
	}
}
