package app.team3.t3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sssbug on 6/30/15.
 */
public class ResDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "res.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_ZIPCODE = "zipcode";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public ResDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the "RESTAURANTS" table
        db.execSQL("create table restaurants (" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "rating real," +
                        "phone text," +
                        "categories blob," +
                        "address blob," +
                        "city text," +
                        "zipcode integer," +
                        "latitude real," +
                        "longitude real)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertRes(Restaurant res) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, res.getName());
        cv.put(COLUMN_RATING, res.getRating());
        cv.put(COLUMN_PHONE, res.getPhone());
        cv.put(COLUMN_CATEGORIES, res.getCategories());
        cv.put(COLUMN_ADDRESS, res.getAddress());
        cv.put(COLUMN_CITY, res.getCity());
        cv.put(COLUMN_ZIPCODE, res.getZipcode());
        cv.put(COLUMN_LATITUDE, res.getLatitude());
        cv.put(COLUMN_LONGITUDE, res.getLongitude());
        return getWritableDatabase().insert(TABLE_RESTAURANTS, null, cv);
    }

    public ResCursor queryRes() {
        Cursor wrapped = getReadableDatabase().query(TABLE_RESTAURANTS, null, null, null, null, null, null);
        return new ResCursor(wrapped);
    }

    public ResCursor queryRes(int id) {
        Cursor wrapped = getReadableDatabase().query(TABLE_RESTAURANTS, null, COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        return new ResCursor(wrapped);
    }

    public class ResCursor extends CursorWrapper {

        public ResCursor(Cursor cursor) {
            super(cursor);
        }

        public Restaurant getRes() {
            if (isBeforeFirst() || isAfterLast())
                return null;
            Restaurant res = new Restaurant();
            res.setId(getInt(getColumnIndex(COLUMN_ID)));
            res.setName(getString(getColumnIndex(COLUMN_NAME)));
            res.setRating(getFloat(getColumnIndex(COLUMN_RATING)));
            res.setPhone(getString(getColumnIndex(COLUMN_PHONE)));
            res.setCategories(getString(getColumnIndex(COLUMN_CATEGORIES)));
            res.setAddress(getString(getColumnIndex(COLUMN_ADDRESS)));
            res.setCity(getString(getColumnIndex(COLUMN_CITY)));
            res.setZipcode(getInt(getColumnIndex(COLUMN_ZIPCODE)));
            res.setLatitude(getFloat(getColumnIndex(COLUMN_LATITUDE)));
            res.setLongitude(getFloat(getColumnIndex(COLUMN_LONGITUDE)));
            return res;
        }
    }

}
