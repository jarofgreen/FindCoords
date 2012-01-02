package uk.co.jarofgreen.FindCoords;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class LoadActivity  extends ListActivity {

	Cursor mCursor;
	
	@Override    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LocationsOpenHelper loh = new LocationsOpenHelper(getBaseContext());
    	SQLiteDatabase db = loh.getReadableDatabase();
    	
    	final String fields[] = { BaseColumns._ID, "title", "lat", "lng" };
    	mCursor =  db.query("locations", fields, null, null, null, null, null);
		ListAdapter adapter = new SimpleCursorAdapter(
                this, 
                R.layout.load_item, 
                mCursor, 
                new String[] {"title","lat","lng"}, 
                new int[] {R.id.title, R.id.lat, R.id.lng});
        setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);		
		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				TextView etLat = (TextView) view.findViewById(R.id.lat);
				Double lat = Double.valueOf(etLat.getText().toString());
				TextView etLng = (TextView) view.findViewById(R.id.lng);
				Double lng = Double.valueOf(etLng.getText().toString());
				
				Intent result = new Intent();
				result.putExtra("latitude",lat);
				result.putExtra("longitude",lng);		
				setResult(RESULT_OK, result);
				finish();
				
			}
		});
	
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.load_item, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		LocationsOpenHelper loh;
		switch (item.getItemId()) {
		case R.id.open:
			loh = new LocationsOpenHelper(getBaseContext());
			Double[] r = loh.getLatLngForId(info.id);
			Intent result = new Intent();
			result.putExtra("latitude",r[0]);
			result.putExtra("longitude",r[1]);		
			setResult(RESULT_OK, result);
			finish();
			return true;
		case R.id.delete:
			loh = new LocationsOpenHelper(getBaseContext());
			loh.deleteId(info.id);
			mCursor.requery();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
}