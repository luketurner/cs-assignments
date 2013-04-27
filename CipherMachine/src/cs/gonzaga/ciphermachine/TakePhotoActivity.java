package cs.gonzaga.ciphermachine;

import java.io.File;
import java.io.IOException;

//import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TakePhotoActivity extends Activity 
{
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected boolean _taken;
	protected Bitmap bitmap;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_take_photo);
       
        _image = ( ImageView ) findViewById( R.id.image );
        _field = ( TextView ) findViewById( R.id.noImageView );
        _button = ( Button ) findViewById( R.id.button );
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _path = Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg";
    }
    
    public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
    		startCameraActivity();
    	}
    }
    
    protected void startCameraActivity()
    {
//    	Log.i("MakeMachine", "startCameraActivity()" );
//    	File file = new File( _path );
//    	Uri outputFileUri = Uri.fromFile( file );
//    	
//    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
//    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
//    	
//    	startActivityForResult( intent, 0 );
    	
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken(data.getExtras());
    			break;
    	}
    }
    
    protected void onPhotoTaken(Bundle extras)
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
//        Bundle extras = intent.getExtras();
        bitmap = (Bitmap) extras.get("data");
//    	Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	_image.setImageBitmap(bitmap);
    }
    
    @Override 
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( TakePhotoActivity.PHOTO_TAKEN ) ) {
    		onPhotoTaken(savedInstanceState);
    	}
    }
    
    @Override
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( TakePhotoActivity.PHOTO_TAKEN, _taken );
    }
    
	public void onPerformOcrClicked(View v) {
//		Intent intent = new Intent(this, TakePhotoActivity.class);
//		startActivity(intent);
		
    	_field.setVisibility( View.GONE );
    	
    	ExifInterface exif;
		try {
			exif = new ExifInterface(_path);
		
	    	int exifOrientation = exif.getAttributeInt(
	    	        ExifInterface.TAG_ORIENTATION,
	    	        ExifInterface.ORIENTATION_NORMAL);
	
	    	int rotate = 0;
	
	    	switch (exifOrientation) {
	    	case ExifInterface.ORIENTATION_ROTATE_90:
	    	    rotate = 90;
	    	    break;
	    	case ExifInterface.ORIENTATION_ROTATE_180:
	    	    rotate = 180;
	    	    break;
	    	case ExifInterface.ORIENTATION_ROTATE_270:
	    	    rotate = 270;
	    	    break;
	    	}
	
	    	if (rotate != 0) {
	    	    int w = bitmap.getWidth();
	    	    int h = bitmap.getHeight();
	
	    	    // Setting pre rotate
	    	    Matrix mtx = new Matrix();
	    	    mtx.preRotate(rotate);
	
	    	    // Rotating Bitmap & convert to ARGB_8888, required by tess
	    	    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
	    	    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String tessDataDirectoryString = Environment.getExternalStorageDirectory() + "/data/";
		// create a File object for the parent directory
		File tessDataDirectory = new File(tessDataDirectoryString);
		// have the object build the directory structure, if needed.
		tessDataDirectory.mkdirs();
		// create a File object for the output file
		File outputFile = new File(tessDataDirectory, "ocr.dat");
		
		//TessBaseAPI baseApi = new TessBaseAPI();
		// DATA_PATH = Path to the storage
		// lang for which the language data exists, usually "eng"
		//baseApi.init(tessDataDirectoryString, "eng"); 
//		baseApi.setImage(bitmap);
//		String recognizedText = baseApi.getUTF8Text();
//		baseApi.end();
	}
}