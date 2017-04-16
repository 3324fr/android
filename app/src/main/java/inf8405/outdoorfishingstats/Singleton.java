package inf8405.outdoorfishingstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by fred on 4/1/17.
 */

class Singleton {

    private static Singleton ourInstance;


    private static DatabaseHelper m_sqLitehelper;

    static Singleton getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new Singleton(context);
        }
        return ourInstance;
    }

    private Singleton(Context context) {
        m_sqLitehelper = new DatabaseHelper(context.getApplicationContext());
    }


    public ArrayList<FishEntry> getFish(){
        SQLiteDatabase db = m_sqLitehelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT * FROM " + SQLiteContract.FishingEntry.TABLE_NAME,null);
        ArrayList<FishEntry>items = new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                FishEntry fish = new FishEntry();
                fish.comment = cursor.getString(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_COMMENT));
                fish.fieldStrength = cursor.getFloat(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_MAG));
                fish.latitude = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_LAT));
                fish.longitude = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_LNG));
                fish.name = cursor.getString(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_NAME));
                fish.pressure = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_PRESSURE));
                fish.temperature = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_TEMPERATURE));

                items.add(fish);
                cursor.moveToNext();
            }
        }

        db.close();
        return items;

    }
}
