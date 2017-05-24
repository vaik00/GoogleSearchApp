package app.dev.googlesearchapp.view.fragment;

import android.view.View;

import java.util.List;

import app.dev.googlesearchapp.model.data.QueryData;

/**
 * Created by vaik00 on 23.05.2017.
 */

public interface ResultView {
    void loadUI(View v);

    void openImageFragment(String path);

    void loadData(String query, int index);

    void updateItem(int position, QueryData data);

    void loadMore(int index);

    void showData(List<QueryData> data);

    void addData(List<QueryData> data);

    void update(String searchQuery);

    void showSuccessSaveSnackbar();

    void showSuccessDeleteSnackbar();
}
