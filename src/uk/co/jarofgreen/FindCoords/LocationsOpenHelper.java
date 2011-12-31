package uk.co.jarofgreen.FindCoords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class LocationsOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "findcoords_locations.db";
    private static final String LOCATIONS_TABLE_NAME = "locations";
            ;

    LocationsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LOCATIONS_TABLE_NAME + " ( "+
        		BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		" title TEXT, "+
        		" lat REAL," +
        		" lng REAL"+
        ");");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
		
	}
}
	

