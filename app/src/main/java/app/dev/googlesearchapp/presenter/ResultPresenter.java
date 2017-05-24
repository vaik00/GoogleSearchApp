package app.dev.googlesearchapp.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import app.dev.googlesearchapp.model.db.QueryType;
import app.dev.googlesearchapp.model.db.SqliteTask;
import app.dev.googlesearchapp.model.db.TaskListener;
import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.model.data.Query;
import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.data.RequestData;
import app.dev.googlesearchapp.view.fragment.ResultView;

import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_ID;
import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_NAME;
import static app.dev.googlesearchapp.model.db.DBHelper.COLUMN_PHOTO;
import static app.dev.googlesearchapp.model.db.DBHelper.TABLE_NAME;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class ResultPresenter {
    private ResultView mResultView;
    private Context mContext;
    private TaskListener mTaskListener;

    public ResultPresenter(ResultView view, Context context, TaskListener listener) {
        this.mResultView = view;
        this.mContext = context;
        this.mTaskListener = listener;
    }

    public void loadData(Query query) {
        List<QueryData> queryData = query.getQueriesDataList();
        RequestData data = query.getIndexRequest().getRequestDatas().get(0);
        int startIndex = data.getStartIndex();
        if (startIndex == 1) {
            if (queryData != null) {
                mResultView.showData(queryData);
            }
        } else {
            if (queryData != null) {
                mResultView.addData(queryData);
            }
        }
    }

    public void updateItem(List<QueryData> currentDataList, QueryData updateData) {
        if (isItemInList(currentDataList, updateData)) {
            int position = getDataPosition(currentDataList, updateData);
            QueryData currentData = currentDataList.get(position);
            currentData.setSelected(updateData.isSelected());
            mResultView.updateItem(position, currentData);
        }
    }

    private boolean isItemInList(List<QueryData> currentDataList, QueryData data) {
        for (int i = 0; i < currentDataList.size(); i++) {
            if (currentDataList.get(i).getTitle().equals(data.getTitle())) {
                return true;
            }
        }
        return false;
    }

    private int getDataPosition(List<QueryData> currentDataList, QueryData data) {
        for (int i = 0; i < currentDataList.size(); i++) {
            if (currentDataList.get(i).getTitle().equals(data.getTitle())) {
                return i;
            }
        }
        return 0;
    }

    public void saveImage(String src, String imageName, Picasso picasso){
        picasso.load(src).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                FileHelper.saveToFile(bitmap, imageName);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public void saveToDb(QueryData data, String imageName) {
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_NAME, data.getTitle());
        newValues.put(COLUMN_PHOTO, imageName);
        SqliteTask sqliteTask = new SqliteTask(mContext,mTaskListener, TABLE_NAME, newValues, null, null, null);
        sqliteTask.execute(QueryType.INSERT);
    }

    public void deleteFromDb(int id, String imageName) {
        SqliteTask sqliteTask = new SqliteTask(mContext,mTaskListener, TABLE_NAME, null, COLUMN_ID + " = " + id, null, null);
        sqliteTask.execute(QueryType.DELETE);
        FileHelper.removeFile(imageName);
    }
}
