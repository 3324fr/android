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

    public void addFish(FishEntry fish){

        SQLiteDatabase db = m_sqLitehelper.getWritableDatabase();


        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_COMMENT, fish.comment);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_LAT, fish.latitude);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_LNG, fish.longintude);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_MAG, fish.fieldStrength);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_NAME, fish.name);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_PRESSURE, fish.pressure);
        values.put(SQLiteContract.FishingEntry.COLUMN_NAME_TEMPERATURE, fish.temperature);
;
        db.insert(SQLiteContract.FishingEntry.TABLE_NAME, null, values);

        db.close();

    }

    public List<FishEntry> getFish(){
        SQLiteDatabase db = m_sqLitehelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select SELECT DISTINCT * from " + SQLiteContract.FishingEntry.TABLE_NAME,null);
        List<FishEntry>items = new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                FishEntry fish = new FishEntry();
                fish.comment = cursor.getString(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_COMMENT));
                fish.fieldStrength = cursor.getFloat(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_MAG));
                fish.latitude = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_LAT));
                fish.longintude = cursor.getInt(cursor.getColumnIndex(SQLiteContract.FishingEntry.COLUMN_NAME_LNG));
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
