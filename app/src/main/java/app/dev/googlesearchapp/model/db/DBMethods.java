package app.dev.googlesearchapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.dev.googlesearchapp.model.data.QueryData;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class DBMethods {
    private SQLiteDatabase db;

    public DBMethods(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void saveContent(String text, String photoPath) {
        ContentValues newValues = new ContentValues();
        newValues.put("name", text);
        newValues.put("photo_path", photoPath);
        db.insert("Favorite", null, newValues);
    }

    public void delete(int id) {
        db.delete("Favorite", "id = " + id, null);
    }


    public List<QueryData> getContentFromDB(int indexStart) {
        List<QueryData> contentList = new ArrayList<>();

        String query = "SELECT  * FROM Favorite LIMIT " + indexStart + ", 10";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                QueryData data = new QueryData();
                data.setTitle(cursor.getString(cursor.getColumnIndex("name")));
                data.setImagePath(cursor.getString(cursor.getColumnIndex("photo_path")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setSelected(true);
                contentList.add(data);
            }
            while (cursor.moveToNext());
        }
        return contentList;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM Favorite";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
