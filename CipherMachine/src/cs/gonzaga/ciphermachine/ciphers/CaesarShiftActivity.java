package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CaesarShiftActivity extends AbstractCipherActivity {
	
	private String inputText = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caesar_shift);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.caesar_shift, menu);
		return true;
	}
	
	public int getKey() {
		return Integer.parseInt(((TextView) findViewById(R.id.caesar_activity_shift)).getText().toString());
	}
	
	public String encipherString() {
		return CaesarShift.encrypt(getInputString(), getKey());
	}
	public String decipherString() {
		return CaesarShift.decrypt(getInputString(), getKey());
	}

}
