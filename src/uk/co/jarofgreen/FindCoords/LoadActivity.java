package uk.co.jarofgreen.FindCoords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadActivity  extends Activity {

	@Override    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.about);
				
		
		Intent result = new Intent();
		result.putExtra("latitude", 1.2345);
		result.putExtra("longitude", 3.4567);		
		setResult(RESULT_OK, result);
		finish();
	}
	
}