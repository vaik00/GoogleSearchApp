package app.dev.googlesearchapp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {

        super(context, "SearchDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);

    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("create table Favorite ("
                + "id integer primary key autoincrement, name text, photo_path text );");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
