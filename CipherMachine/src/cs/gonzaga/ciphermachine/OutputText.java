package cs.gonzaga.ciphermachine;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
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

	public void onTextInputClick(View v) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:"));
		smsIntent.putExtra("sms_body", outputText);
		startActivity(smsIntent);
	}
	
	public void onEmailInputClick(View v) {
	     Intent emailIntent = new Intent(Intent.ACTION_SEND);
	     emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
	     emailIntent.putExtra(Intent.EXTRA_SUBJECT, "iCrypt");
	     emailIntent.putExtra(Intent.EXTRA_TEXT, outputText);
	     emailIntent.setType("plain/text");
	     startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
	
	public void onTweetInputClick(View v) {
		String tweetUrl = "https://twitter.com/intent/tweet?text=" + outputText + " &url=";
		Uri tweetUri = Uri.parse(tweetUrl);
		startActivity(new Intent(Intent.ACTION_VIEW, tweetUri));
	}
}