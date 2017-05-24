package app.dev.googlesearchapp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vaik00 on 22.05.2017.
 */
 public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Favourite";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHOTO = "photo_path";

    DBHelper(Context context) {

        super(context, "SearchDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);

    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("
                + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NAME + " text, " + COLUMN_PHOTO + " text );");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
