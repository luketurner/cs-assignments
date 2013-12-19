package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputText extends Activity {
	Button buttonInputClick;
	EditText editTextInput;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);
        
        buttonInputClick = (Button)findViewById(R.id.buttonInput);
        editTextInput = (EditText)findViewById(R.id.editTextInput);
        editTextInput.setOnEditorActionListener(new DoneOnEditorActionListener());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input_text, menu);
        return true;
    }
    
    public void onInputClick(View v) {
    	String inputText = editTextInput.getText().toString(); 	
        Intent intent = new Intent(this, CipherSelector.class);
        intent.putExtra("inputText", inputText);
        this.startActivity(intent);
    }
}