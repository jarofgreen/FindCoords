package uk.co.jarofgreen.FindCoords;

import android.app.Activity;
import android.os.Bundle;

public class SaveActivity  extends Activity {

	@Override    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.about);
		
		
		setResult(RESULT_OK, null);
		finish();

	}
	
}