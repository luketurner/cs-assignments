package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class CaesarShiftActivity extends Activity {
	
	String outputText = "";

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
	
	public void onButtonClick(View v) {
		String actionType = (String) v.getTag();
		Intent intent = new Intent();
		String outputText = "";
		if (actionType.equals("Encrypt")) {
			intent.setClassName("cs.gonzaga.ciphermachine", "cs.gonzaga.ciphermachine.OutputText");
			outputText = CaesarShift.encrypt(outputText);
		}
		intent.putExtra("outputText", outputText);
		this.startActivity(intent);
	}

}
