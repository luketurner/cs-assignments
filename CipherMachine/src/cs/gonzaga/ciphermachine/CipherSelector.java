package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class CipherSelector extends Activity {
	
	String inputText = "test";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cipher_selector);

		inputText = getIntent().getExtras().getString("inputText");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cipher_selector, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onCipherSelected(View v) {
		String cipherName = (String) v.getTag();
		Intent intent = new Intent();
		intent.putExtra("inputText", inputText);
		if (cipherName.equals(getResources().getString(R.string.cipher_select_caesar))) {
			intent.setClass(this, cs.gonzaga.ciphermachine.ciphers.CaesarShiftActivity.class);
		}
		if (cipherName.equals(getResources().getString(R.string.cipher_select_caesar_box))) {
			intent.setClass(this, cs.gonzaga.ciphermachine.ciphers.ShiftedCaesarsBoxActivity.class);
		}
		if (cipherName.equals(getResources().getString(R.string.cipher_select_rail_fence))) {
			intent.setClass(this, cs.gonzaga.ciphermachine.ciphers.RailFenceActivity.class);
		}
		this.startActivity(intent);
	}

}
