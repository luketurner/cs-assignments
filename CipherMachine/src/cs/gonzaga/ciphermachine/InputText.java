package cs.gonzaga.ciphermachine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class InputText extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);

        Intent intent = new Intent();
        intent.setClassName(this, "cs.gonzaga.ciphermachine.CipherSelector");
        intent.putExtra("inputText", "This is a test.");
        this.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input_text, menu);
        return true;
    }
    
}
