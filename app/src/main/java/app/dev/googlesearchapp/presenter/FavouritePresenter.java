package app.dev.googlesearchapp.presenter;

import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.model.db.DBMethods;
import app.dev.googlesearchapp.view.fragment.FavouriteView;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class FavouritePresenter {
    private FavouriteView mFavouriteView;
    private DBMethods mDB;

    public FavouritePresenter(FavouriteView view, DBMethods db) {
        mFavouriteView = view;
        mDB = db;
    }

    public void loadData(int index) {
        if (index == 0)
            mFavouriteView.showData(mDB.getContentFromDB(index));
        else
            mFavouriteView.addData(mDB.getContentFromDB(index));
    }

    public void deleteFromDb(int id, int position, String imageName) {
        mDB.delete(id);
        FileHelper.removeFile(imageName);
        mFavouriteView.deleteItem(position);
        mFavouriteView.showSuccessDeleteSnackbar();
    }


}
