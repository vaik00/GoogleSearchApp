package app.dev.googlesearchapp.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.model.data.Query;
import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.data.RequestData;
import app.dev.googlesearchapp.model.db.DBMethods;
import app.dev.googlesearchapp.view.fragment.ResultView;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class ResultPresenter {
    private ResultView mResultView;
    private DBMethods mDB;

    public ResultPresenter(ResultView view, DBMethods db) {
        mResultView = view;
        mDB = db;
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

    public void saveToDb(QueryData data, Picasso picasso) {
        String imageName = Long.toString(System.currentTimeMillis()) + ".jpeg";
        String src = data.getPagemap().getCseThumbnailData().get(0).getSrc();
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
        mDB.saveContent(data.getTitle(), imageName);
        mResultView.showSuccessSaveSnackbar();
    }

    public void deleteFromDb(int id, String imageName) {
        mDB.delete(id);
        FileHelper.removeFile(imageName);
        mResultView.showSuccessDeleteSnackbar();
    }
}
