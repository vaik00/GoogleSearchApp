package app.dev.googlesearchapp.view.adapter;

import app.dev.googlesearchapp.model.data.QueryData;

/**
 * Created by vaik00 on 23.05.2017.
 */

public interface ActionListener {
    void openImage(String path);

    void saveToDB(QueryData data);

    void deleteFromDB(int id, int position, String imageName);
}
