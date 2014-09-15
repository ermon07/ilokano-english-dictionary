package edu.ucuccs.diksyonaryo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Meaning extends Activity {

	TextView wordId;
	TextView wordTitle;
	TextView wordAuthor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meaning);

		wordId = (TextView) findViewById(R.id.resID);
		wordTitle = (TextView) findViewById(R.id.resTitle);
		wordAuthor = (TextView) findViewById(R.id.resAuthor);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			wordId.setText(extras.getString("wordId"));
			wordTitle.setText(extras.getString("wordTitle"));
			wordAuthor.setText(extras.getString("wordAuthor"));

		} else {
			Toast.makeText(getApplicationContext(), "wala", Toast.LENGTH_LONG)
					.show();
		}

	}

}
