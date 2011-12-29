package uk.co.jarofgreen.FindCoords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private LocationManager locationManager;
	private Location currentLocation;
	private Location desiredLocation;
	
	private TextView tvCurrentLat;
	private TextView tvCurrentLng;
	private TextView tvCurrentAccuracy;
	private TextView tvCurrentBearing;
	private EditText etDesiredLat;
	private EditText etDesiredLng;
	private TextView tvDistance;
	private TextView tvBearing;
	
	private ImageView imgBearing;
	private Bitmap compassImageNorth;
	private Bitmap compassImageNorthEast;
	private Bitmap compassImageNorthWest;
	private Bitmap compassImageEast;
	private Bitmap compassImageWest;
	private Bitmap compassImageSouth;


	
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float currentBearing = 0;
	
	private final SensorEventListener mListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			currentBearing = event.values[0];
			update();
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			currentLocation = location;
			update();
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onProviderDisabled(String provider) {}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tvCurrentLat = (TextView) findViewById(R.id.currentLat);
		tvCurrentLng = (TextView) findViewById(R.id.currentLng);
		tvCurrentAccuracy = (TextView) findViewById(R.id.currentAccuracy);
		tvCurrentBearing =  (TextView) findViewById(R.id.currentBearing);
		
		etDesiredLat = (EditText) findViewById(R.id.desiredLat);
		etDesiredLat.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) { updateDesiredLocation(); }
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){ updateDesiredLocation(); }
		});
		
		etDesiredLng = (EditText) findViewById(R.id.desiredLng);
		etDesiredLng.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) { updateDesiredLocation(); }
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){ updateDesiredLocation(); }
		}); 
		
		tvDistance = (TextView) findViewById(R.id.desiredDistance);
		tvBearing = (TextView) findViewById(R.id.desiredBearing);
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);		
		
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); 
		
		imgBearing = (ImageView) findViewById(R.id.bearing);
		compassImageNorth = BitmapFactory.decodeResource(getResources(), R.drawable.compass_n);
		compassImageNorthEast = BitmapFactory.decodeResource(getResources(), R.drawable.compass_ne);
		compassImageNorthWest = BitmapFactory.decodeResource(getResources(), R.drawable.compass_nw);
		compassImageEast = BitmapFactory.decodeResource(getResources(), R.drawable.compass_e);
		compassImageWest = BitmapFactory.decodeResource(getResources(), R.drawable.compass_w);
		compassImageSouth = BitmapFactory.decodeResource(getResources(), R.drawable.compass_s);
		
		update();
	}

    @Override
    protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mListener, mSensor,SensorManager.SENSOR_DELAY_GAME);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);        
    }

    @Override
    protected void onStop() {
		locationManager.removeUpdates(locationListener);
		mSensorManager.unregisterListener(mListener);
		super.onStop();
    }
    	
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.map_menu_item:
        	String uri = "geo:"+Double.toString(desiredLocation.getLatitude())
        	    +","+Double.toString(desiredLocation.getLongitude())
        	    +"?q="+Double.toString(desiredLocation.getLatitude())
        	    +","+Double.toString(desiredLocation.getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse(uri));
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void updateDesiredLocation() {
		try { 
			double lat = Double.parseDouble(etDesiredLat.getText().toString());
			double lng = Double.parseDouble(etDesiredLng.getText().toString());
			desiredLocation = new Location(LocationManager.GPS_PROVIDER);
			desiredLocation.setLatitude(lat);
			desiredLocation.setLongitude(lng);			
		} catch (NumberFormatException e) {
			desiredLocation = null;
		}		
		update();
	}
	
	public void update() {
		
		tvCurrentBearing.setText(Float.toString(currentBearing));
		
		if (currentLocation instanceof Location) {
					
			double currentLat = currentLocation.getLatitude();
			tvCurrentLat.setText(Double.toString(currentLat));
								
			double currentLng = currentLocation.getLongitude();
			tvCurrentLng.setText(Double.toString(currentLng));

			float currentAccuracy = currentLocation.getAccuracy();
			tvCurrentAccuracy.setText(Float.toString(currentAccuracy));
			
			if (desiredLocation instanceof Location) {
						
				float bearing = currentLocation.bearingTo(desiredLocation) ;
				tvBearing.setText(Float.toString(bearing));

				float distance = currentLocation.distanceTo(desiredLocation);
					
				
				if (distance > 1) {
					tvDistance.setText(Float.toString(distance));
					float bearingRelative = bearing - currentBearing;
					while (bearingRelative < 0) bearingRelative =  bearingRelative + 360;
					while (bearingRelative > 360) bearingRelative =  bearingRelative - 360;
					if (bearingRelative <= 22) {
						imgBearing.setImageBitmap(compassImageNorth);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (22 < bearingRelative  && bearingRelative <= 68) {
						imgBearing.setImageBitmap(compassImageNorthEast);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (68 < bearingRelative && bearingRelative <= 135) {
						imgBearing.setImageBitmap(compassImageEast);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (138 < bearingRelative && bearingRelative <= 225) {
						imgBearing.setImageBitmap(compassImageSouth);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (225 < bearingRelative && bearingRelative <= 295) {
						imgBearing.setImageBitmap(compassImageWest);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (295 < bearingRelative && bearingRelative <= 338) {
						imgBearing.setImageBitmap(compassImageNorthWest);
						imgBearing.setVisibility(View.VISIBLE);
					} else if (338 < bearingRelative) {
						imgBearing.setImageBitmap(compassImageNorth);
						imgBearing.setVisibility(View.VISIBLE);
					}
				} else {
					tvDistance.setText("Less than 1");
					imgBearing.setVisibility(View.INVISIBLE);
				}
				
				
			} else {
				tvDistance.setText("destination not parsed");
				tvBearing.setText("destination not parsed");
			}
			
			
		} else {

			tvCurrentLat.setText("dunno");
			tvCurrentLng.setText("dunno");
			tvCurrentAccuracy.setText("dunno");
			tvDistance.setText("dunno");
			tvBearing.setText("dunno");
			
		}
		
	}
	
}