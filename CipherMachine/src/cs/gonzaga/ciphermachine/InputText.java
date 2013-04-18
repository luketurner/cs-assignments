package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputText extends Activity {
	Button buttonEncypherClick;
	Button buttonDecypherClick;
	EditText editTextInput;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);
        
        buttonEncypherClick = (Button)findViewById(R.id.buttonEncypher);
        buttonDecypherClick = (Button)findViewById(R.id.buttonDecypher);
        editTextInput = (EditText)findViewById(R.id.editTextInput);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input_text, menu);
        return true;
    }
    
    public void onEncypherClick(View v) {
    	String inputText = editTextInput.getText().toString(); 	
//    	editTextInput.setText("ENCYPHER_TEXT: " + inputText);
        Intent intent = new Intent(this, CipherSelector.class);
//        intent.setClassName("cs.gonzaga.ciphermachine", "CipherSelector");
        intent.putExtra("inputText", inputText);
        this.startActivity(intent);
    }
    
    public void onDecypherClick(View v) {
    	String inputText = editTextInput.getText().toString();
//    	editTextInput.setText("DECYPHER_TEXT: " + inputText);
        Intent intent = new Intent(this, CipherSelector.class);
//        intent.setClassName("cs.gonzaga.ciphermachine", "CipherSelector");
        intent.putExtra("inputText", inputText);
        this.startActivity(intent);
    }    
}