package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ChooseInputType extends Activity {
	Button buttonManualClick;
	Button buttonFileClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_input_type);
		
        buttonManualClick = (Button)findViewById(R.id.buttonManualInput);
        buttonFileClick = (Button)findViewById(R.id.buttonFileInput);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_input_type, menu);
		return true;
	}

	public void onManualClicked(View v) {
		Intent intent = new Intent(this, InputText.class);
		startActivity(intent);
	}
	
	public void onFileClicked(View v) {
		
	}
}
