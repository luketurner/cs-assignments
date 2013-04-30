package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ADFGVXActivity extends AbstractCipherActivity {
	
	public String getKey() {
		return ((TextView) findViewById(R.id.adfgvx_activity_keyword)).getText().toString();
	}
	
	public String encipherString() {
		return ADFGVXCipher.encode(getKey(), getInputString());
	}
	public String decipherString() {
		return ADFGVXCipher.decode(getKey(), getInputString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adfgvx);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adfgvx, menu);
		return true;
	}

}
