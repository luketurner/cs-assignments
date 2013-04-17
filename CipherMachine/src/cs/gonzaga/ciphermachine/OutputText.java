package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class OutputText extends Activity {
	
	String outputText = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			outputText = getIntent().getExtras().getString("outputText");
			System.out.print("Output Text:"); System.out.println(outputText);//DEBUG
		} catch (NullPointerException e) {
			outputText = "ERROR";
			System.out.println("Output Text Not Found in Output Activity Bundle.");
			System.out.println(savedInstanceState.keySet());
		}
		setContentView(R.layout.activity_output_text);
	}
	
	protected void onStart() {
		super.onStart();
		TextView out = (TextView) findViewById(R.id.outputTextLabel);
		if (out != null ) {out.setText(outputText);}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.output_text, menu);
		return true;
	}

}
