
package app.team3.t3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String COLUMN_REVIEW_COUNT = "review_count";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_ZIPCODE = "zipcode";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_BUSINESS_IMG = "business_img";
    private static final String COLUMN_RATING_IMG = "rating_img";

    public ResDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Create Database for the Yum application.
     * Store the results from Yelp.
     *
     * @param db
     */
    public void createDB(SQLiteDatabase db) {
        //Create the "restaurants" table
        db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_RATING + " REAL, " +
                        COLUMN_REVIEW_COUNT + " INTEGER, " +
                        COLUMN_PHONE + " TEXT, " +
                        COLUMN_CATEGORIES + " BLOB, " +
                        COLUMN_ADDRESS + " BLOB, " +
                        COLUMN_CITY + " TEXT, " +
                        COLUMN_ZIPCODE + " INTEGER, " +
                        COLUMN_LATITUDE + " REAL, " +
                        COLUMN_LONGITUDE + " REAL," +
                        COLUMN_BUSINESS_IMG + " BLOB, " +
                        COLUMN_RATING_IMG + " BLOB)"
        );
    }

    /**
     * If the Restaurants table exists, drop the table. If not exsits, create a new one.
     * The program needs to refresh the database every time when it requests a search from Yelp.
     * Otherwise, user will get old datas from database.
     */
    public void updateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        createDB(db);
    }


    /**
     * Insert a single Restaurant object into the database
     *
     * @param res
     */
    public void insertRestaurant(Restaurant res) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, res.getName());
        contentValues.put(COLUMN_RATING, res.getRating());
        contentValues.put(COLUMN_PHONE, res.getPhone());
        contentValues.put(COLUMN_REVIEW_COUNT, res.getReviewCount());
        contentValues.put(COLUMN_CATEGORIES, res.getCategories());
        contentValues.put(COLUMN_ADDRESS, res.getAddress());
        contentValues.put(COLUMN_CITY, res.getCity());
        contentValues.put(COLUMN_ZIPCODE, res.getZipCode());
        contentValues.put(COLUMN_LATITUDE, res.getLatitude());
        contentValues.put(COLUMN_LONGITUDE, res.getLongitude());
        contentValues.put(COLUMN_BUSINESS_IMG, res.getBusinessImgURL());
        contentValues.put(COLUMN_RATING_IMG, res.getRatingImgURL());

        db.insert(TABLE_RESTAURANTS, null, contentValues);

        db.close();
    }

    /**
     * Insert an array of object restaurants into database
     *
     * @param restaurants
     */
    public void insertRestaurants(Restaurant[] restaurants) {
        updateDB();
        for (int i = 0; i < restaurants.length; i++) {
            insertRestaurant(restaurants[i]);
        }
    }

    /**
     * use the param id to pull out the data from database
     * return an object restaurant
     *
     * @param id
     * @return Restaurant
     */
    public Restaurant getRestaurant(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{"*"}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Restaurant restaurant = new Restaurant(cursor.getString(1), Float.parseFloat(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), Integer.parseInt(cursor.getString(8)),
                Float.parseFloat(cursor.getString(9)), Float.parseFloat(cursor.getString(10)), cursor.getString(11), cursor.getString(12));

        return restaurant;
    }
}



