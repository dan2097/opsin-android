package uk.ac.cam.ch.wwmm.opsin.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TouchImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class OPSIN extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText editText = (EditText) findViewById(R.id.NameInput);
        final Button submitButton = (Button) findViewById(R.id.SubmitButton);
        final TouchImageView imageView = (TouchImageView) findViewById(R.id.OpsinOutput);
        submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Context context = imageView.getContext();
				Drawable image;
				try {
					image = ImageOperations(context,"http://opsin.ch.cam.ac.uk/opsin/" +URLEncoder.encode(editText.getText().toString(), "UTF-8")+".png");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("JVM doesn't support UTF-8!");
				}
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				imageView.setImageDrawable(image);
				if (image==null){
					Toast toast = Toast.makeText(context, "OPSIN request failed!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
            }
       });
        
        final ZoomControls zoomControls = (ZoomControls) findViewById(R.id.ZoomControls);
        zoomControls.setOnZoomInClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Matrix mtrx = imageView.getTransformationMatrix();
				mtrx.postScale(1.1f, 1.1f);
				imageView.setImageMatrix(mtrx); 
			}
		});
        
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Matrix mtrx = imageView.getTransformationMatrix();
				mtrx.postScale(0.9f, 0.9f);
				imageView.setImageMatrix(mtrx); 
			}
		});
    }
    
	private Drawable ImageOperations(Context ctx, String url) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

}