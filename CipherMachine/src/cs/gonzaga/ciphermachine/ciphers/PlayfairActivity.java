package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class PlayfairActivity extends AbstractCipherActivity {
	
	public String getKeyword() {
		return ((TextView) findViewById(R.id.playfair_activity_key)).getText().toString();
	}
	
	public String encipherString() {
		return PlayfairCipher.encode(getKeyword(), getInputString());
	}
	
	public String decipherString() {
		return PlayfairCipher.decode(getKeyword(), getInputString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playfair);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playfair, menu);
		return true;
	}

}
