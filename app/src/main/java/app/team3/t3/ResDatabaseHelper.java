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
    // Database Name
    private static final String DB_NAME = "res.sqlite";

    // Database Version
    private static final int VERSION = 1;

    // Restaurant table Name
    private static final String TABLE_RESTAURANTS = "restaurants";

    // Restaurant info Columns names
    private static final String COLUMN_ID = "id";
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
        db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_RATING + " REAL, " +
                        COLUMN_PHONE + " TEXT, " +
                        COLUMN_CATEGORIES + " BLOB, " +
                        COLUMN_ADDRESS + " BLOB, " +
                        COLUMN_CITY + " TEXT, " +
                        COLUMN_ZIPCODE + " INTEGER, " +
                        COLUMN_LATITUDE + " REAL, " +
                        COLUMN_LONGITUDE + " REAL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);

        // Create table again
        onCreate(db);
    }

    public void insertRestaurant(Restaurant res) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, res.getName());
        contentValues.put(COLUMN_RATING, res.getRating());
        contentValues.put(COLUMN_PHONE, res.getPhone());
        contentValues.put(COLUMN_CATEGORIES, res.getCategories());
        contentValues.put(COLUMN_ADDRESS, res.getAddress());
        contentValues.put(COLUMN_CITY, res.getCity());
        contentValues.put(COLUMN_ZIPCODE, res.getZipcode());
        contentValues.put(COLUMN_LATITUDE, res.getLatitude());
        contentValues.put(COLUMN_LONGITUDE, res.getLongitude());

        db.insert(TABLE_RESTAURANTS, null, contentValues);

        db.close();
    }

    public Restaurant getRestaurant(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{"*"}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Restaurant restaurant = new Restaurant(cursor.getString(1), Float.parseFloat(cursor.getString(2)), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), Integer.parseInt(cursor.getString(7)),
                Float.parseFloat(cursor.getString(8)), Float.parseFloat(cursor.getString(9)));

        return restaurant;
    }
    /*
    public ResCursor queryRes() {
        Cursor wrapped = getReadableDatabase().query(TABLE_RESTAURANTS, null, null, null, null, null, null);
        return new ResCursor(wrapped);
    }

    public ResCursor queryRes(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{"*"}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Restaurant restaurant = new Restaurant(cursor.getString(0), Float.parseFloat(cursor.getString(1)), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Float.parseFloat(cursor.getString(7)), Float.parseFloat(cursor.getString(8)));

        return new ResCursor(cursor);
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
    */

}
