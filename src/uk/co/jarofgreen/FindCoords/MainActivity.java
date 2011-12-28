package uk.co.jarofgreen.FindCoords;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	LocationListener locationListener;

	Location currentLocation;
	TextView tvCurrentLat;
	TextView tvCurrentLng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tvCurrentLat = (TextView) findViewById(R.id.currentLat);
		tvCurrentLng = (TextView) findViewById(R.id.currentLng);
		
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				currentLocation = location;
				update();
			}
			
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			public void onProviderEnabled(String provider) {}
			
			public void onProviderDisabled(String provider) {}
		};
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		
		currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		update();
	}
	
	public void update() {
		
		if (currentLocation instanceof Location) {
					
			double currentLat = currentLocation.getLatitude();
			tvCurrentLat.setText(Double.toString(currentLat));
								
			double currentLng = currentLocation.getLongitude();
			tvCurrentLng.setText(Double.toString(currentLng));

		} else {

			tvCurrentLat.setText("dunno");
			tvCurrentLng.setText("dunno");
			
			
		}
		
	}
	
}