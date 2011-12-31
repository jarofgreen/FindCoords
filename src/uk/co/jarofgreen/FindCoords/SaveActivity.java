package uk.co.jarofgreen.FindCoords;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveActivity  extends Activity {

	double lat, lng;
	
	@Override    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save);

        Bundle extras = getIntent().getExtras();
       	lat = extras.getDouble("latitude");
       	lng = extras.getDouble("longitude");
		
		final Button buttonOK = (Button) findViewById(R.id.ok);
		buttonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	EditText et = (EditText) findViewById(R.id.entry);

            	LocationsOpenHelper loh = new LocationsOpenHelper(getBaseContext());
            	SQLiteDatabase db = loh.getWritableDatabase();
            	ContentValues cv = new ContentValues();
            	cv.put("lat", Double.valueOf(lat));
            	cv.put("lng", Double.valueOf(lng));
            	cv.put("title", et.getText().toString());
            	long newID = db.insert("locations", null, cv);
            	
            	if (newID != -1) {
	            	setResult(RESULT_OK, null);
	        		finish();
            	}
            }
        });
		

		final Button buttonCancel = (Button) findViewById(R.id.cancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	setResult(RESULT_CANCELED, null);
        		finish();
            }
        });

	}
	
}