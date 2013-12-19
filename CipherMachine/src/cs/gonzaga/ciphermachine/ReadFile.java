package cs.gonzaga.ciphermachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ipaulpro.afilechooser.utils.FileUtils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadFile extends Activity {
	protected TextView fileOutputView;
	protected String passFile = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_file);
		fileOutputView = ( TextView ) findViewById( R.id.viewFileInputView );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_file, menu);
		return true;
	}
	
	public void onLoadClicked(View v) {
	    Intent target = FileUtils.createGetContentIntent();
	    Intent intent = Intent.createChooser(target, "Choose File");
	    
	    startActivityForResult(intent, 1234);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    String readIn = "";
		switch (requestCode) {
	    case 1234:  
	        if (resultCode == RESULT_OK) {  
	            // The URI of the selected file 
	            final Uri uri = data.getData();
	            // Create a File from this Uri
	            File file = FileUtils.getFile(uri);
	            try {
					FileInputStream fileStream = new FileInputStream(file);
					byte[] input = new byte[fileStream.available()];
					while(fileStream.read(input) != -1) {
						readIn += new String(input);
					}
					fileStream.close();
					Log.v("ReadFile.java", "File read in " + readIn);
					fileOutputView.setText(readIn);
					passFile = readIn;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	        }
	    }
	}
	
	public void onChooseCipherClicked(View v) {
    	String inputText = passFile; 	
        Intent intent = new Intent(this, CipherSelector.class);
        intent.putExtra("inputText", inputText);
        this.startActivity(intent);
	}
	
	
}
