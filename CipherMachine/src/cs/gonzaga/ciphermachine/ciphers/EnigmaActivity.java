package cs.gonzaga.ciphermachine.ciphers;

import cs.gonzaga.ciphermachine.R;
import cs.gonzaga.ciphermachine.R.layout;
import cs.gonzaga.ciphermachine.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Spinner;
import android.widget.TextView;

public class EnigmaActivity extends AbstractCipherActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enigma);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enigma, menu);
		return true;
	}
	
	public String getSpinnerSelectionById(int id) {
		Spinner s = (Spinner) findViewById(id);
		String item = (String) s.getSelectedItem();
		return item;
	}
	
	public void setEnigmaKeyValues() {
		String rotorOne = getSpinnerSelectionById(R.id.enigma_rotor_select_first);
		String rotorTwo = getSpinnerSelectionById(R.id.enigma_rotor_select_second);
		String rotorThree = getSpinnerSelectionById(R.id.enigma_rotor_select_third);
		String charOne = getSpinnerSelectionById(R.id.enigma_chars_first);
		String charTwo = getSpinnerSelectionById(R.id.enigma_chars_second);
		String charThree = getSpinnerSelectionById(R.id.enigma_chars_third);
		String ringOne = getSpinnerSelectionById(R.id.enigma_ring_chars_first);
		String ringTwo = getSpinnerSelectionById(R.id.enigma_ring_chars_second);
		String ringThree = getSpinnerSelectionById(R.id.enigma_ring_chars_third);
		String reflector = getSpinnerSelectionById(R.id.enigma_select_reflector);
		cs.gonzaga.ciphermachine.ciphers.enigma.Enigma.setKeyValues(rotorOne, rotorTwo, rotorThree, charOne, charTwo, charThree, ringOne, ringTwo, ringThree, reflector);
		
	}
	
	public String encipherString() {
		setEnigmaKeyValues();
		return cs.gonzaga.ciphermachine.ciphers.enigma.Enigma.encode(getInputString());
	}
	
	public String decipherString() {
		setEnigmaKeyValues();
		return cs.gonzaga.ciphermachine.ciphers.enigma.Enigma.decode(getInputString());
	}

}
