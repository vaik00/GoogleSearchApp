package app.dev.googlesearchapp.model.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import static app.dev.googlesearchapp.model.db.DBHelper.TABLE_NAME;

/**
 * Created by vaik00 on 24.05.2017.
 */

public class SqliteTask extends AsyncTask<QueryType, Void, Object> {

    private TaskListener mTaskListener;
    private SQLiteDatabase db;
    private ContentValues values;
    private String table, where;
    private String[] whereArgs;
    private QueryType queryType;
    private Integer indexStart;

    public SqliteTask(Context context,TaskListener listener,  String table, ContentValues values, String where, String[] whereArgs, Integer index) {
        this.mTaskListener = listener;
        this.table = table;
        DBHelper dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
        this.values = values;
        this.where = where;
        this.whereArgs = whereArgs;
        this.indexStart = index;
    }

    @Override
    protected Object doInBackground(QueryType... params) {
        queryType = params[0];

        switch (queryType) {
            case INSERT:
                return db.insert(table, null, values);
            case DELETE:
                return db.delete(table, where, whereArgs);
            case SELECT:
                String query = "SELECT  * FROM " + TABLE_NAME + " LIMIT " + indexStart + ", 10";
                return db.rawQuery(query, null);
        }
        return db.query(table, null, null, null, null, null, null);
    }

    @Override
    protected void onPostExecute(Object result) {
        mTaskListener.onTaskCompleted(queryType, result);
        db.close();
    }
}