package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OutputText extends Activity {
	
	String outputText = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		outputText = savedInstanceState.getString("outputText");
		setContentView(R.layout.activity_output_text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.output_text, menu);
		return true;
	}

}
