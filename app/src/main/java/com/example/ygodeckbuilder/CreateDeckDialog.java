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
 * Dialog to add a new deck
 */
public class CreateDeckDialog extends AppCompatDialogFragment {

	private EditText deckNameEditText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_create_deck, null);

		builder.setView(view).setTitle("Add a new Deck")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// nothing to do, just close the dialog
					}
				})
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// add the new deck with the name typed by the user, save, notify the user with a toast
						MainActivity.deckList.put(deckNameEditText.getText().toString(), new Vector<String>());
						MainActivity.backup();
						Toast.makeText(getContext(), "New deck added", Toast.LENGTH_SHORT).show();
						MainActivity.staticRefresh();
					}
				});

		deckNameEditText = view.findViewById(R.id.newDeckName);

		return builder.create();
	}
}
