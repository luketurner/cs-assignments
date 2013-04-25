package cs.gonzaga.ciphermachine.ciphers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cs.gonzaga.ciphermachine.R;

public abstract class AbstractCipherActivity extends Activity {
	private String inputString = null;
	
	public String getInputString() {
		return inputString;
	}
	
	public void setInputString(String newInputString) {
		inputString = newInputString;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setInputString(getIntent().getExtras().getString("inputText"));
	}
	
	public void onEncipherCall(View v) {
		Intent intent = new Intent();
		intent.setClass(this, cs.gonzaga.ciphermachine.OutputText.class);
		intent.putExtra("outputText", encipherString());
		this.startActivity(intent);
	}
	
	public void onDecipherCall(View v) {
		Intent intent = new Intent();
		intent.setClass(this, cs.gonzaga.ciphermachine.OutputText.class);
		intent.putExtra("outputText", decipherString());
		this.startActivity(intent);
	}
	
	public abstract String encipherString();
	public abstract String decipherString();

}
