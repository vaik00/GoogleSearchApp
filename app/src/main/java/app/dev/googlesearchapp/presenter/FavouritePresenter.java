package app.dev.googlesearchapp.presenter;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.db.QueryType;
import app.dev.googlesearchapp.model.db.SqliteTask;
import app.dev.googlesearchapp.model.db.TaskListener;
import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.view.fragment.FavouriteView;

import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_ID;
import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_NAME;
import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_PHOTO;
import static app.dev.googlesearchapp.model.db.DBHelper.TABLE_NAME;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class FavouritePresenter {
    private FavouriteView mFavouriteView;
    private boolean mIsLoadMore = false;
    private Context mContext;
    private TaskListener mTaskListener;

    public FavouritePresenter(FavouriteView view, Context context, TaskListener listener) {
        mFavouriteView = view;
        this.mContext = context;
        this.mTaskListener = listener;
    }

    public void loadData(int index, boolean isLoadMore) {
        mIsLoadMore = isLoadMore;
        SqliteTask task = new SqliteTask(mContext, mTaskListener, TABLE_NAME, null, null, null, index);
        task.execute(QueryType.SELECT);
    }

    public void deleteFromDb(int id, int position, String imageName) {
        SqliteTask sqliteTask = new SqliteTask(mContext, mTaskListener, TABLE_NAME, null, COLUMN_ID + " = " + id, null, null);
        sqliteTask.execute(QueryType.DELETE);
        FileHelper.removeFile(imageName);
        mFavouriteView.deleteItem(position);
    }

    public void readData(Cursor cursor) {
        List<QueryData> contentList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                QueryData data = new QueryData();
                data.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                data.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)));
                data.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                data.setSelected(true);
                contentList.add(data);
            }
            while (cursor.moveToNext());
        }
        if (mIsLoadMore)
            mFavouriteView.addData(contentList);
        else
            mFavouriteView.showData(contentList);
    }


}
