package inf8405.outdoorfishingstats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by fred on 4/1/17.
 */

public final class SQLiteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SQLiteContract() {}

    /* Inner class that defines the table contents */
    public static class FishingEntry implements BaseColumns {
        public static final String TABLE_NAME = "fishing";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MAG = "mag";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_PRESSURE = "pressure";
        public static final String COLUMN_NAME_LAT = "lattitude";
        public static final String COLUMN_NAME_LNG = "longitude";
    }




    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FishingEntry.TABLE_NAME + " (" +
                    FishingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FishingEntry.COLUMN_NAME_COMMENT + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_MAG + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_TEMPERATURE + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_PRESSURE + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_LAT + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_COMMENT + " TEXT NOT NULL," +
                    FishingEntry.COLUMN_NAME_LNG + " TEXT NOT NULL) ";



    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FishingEntry.TABLE_NAME;
}
