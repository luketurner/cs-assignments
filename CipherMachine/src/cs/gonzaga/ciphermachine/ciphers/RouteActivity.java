package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RouteActivity extends AbstractCipherActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route, menu);
		return true;
	}

	public String getSpinnerSelectionById(int id) {
		Spinner s = (Spinner) findViewById(id);
		String item = (String) s.getSelectedItem();
		return item;
	}
	
	public int getRailLength() {
		return Integer.parseInt(((TextView) findViewById(R.id.route_activity_length)).getText().toString());
	}
	
	public boolean getTopStart() {
		return ((ToggleButton) findViewById(R.id.route_activity_top)).isChecked();
	}

	public boolean getLeftStart() {
		return ((ToggleButton) findViewById(R.id.route_activity_left)).isChecked();
	}
	
	public int getRoutePath() {
		RadioGroup rGroup = (RadioGroup)findViewById(R.id.route_select_route);
		RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
		if (checkedRadioButton.getTag().equals("1")) {
			return 1;
		}
		else {
			return 2;
		}
	}
	
	public String encipherString() {
		String placeholder = getSpinnerSelectionById(R.id.route_activity_placeholder);
		return RouteCipher.encode(getInputString(), getRailLength(), getTopStart(), getLeftStart(), getRoutePath(), placeholder);
	}
	
	public String decipherString() {
		String placeholder = getSpinnerSelectionById(R.id.route_activity_placeholder);
		return RouteCipher.decode(getInputString(), getRailLength(), getTopStart(), getLeftStart(), getRoutePath(), placeholder);
	}

}
