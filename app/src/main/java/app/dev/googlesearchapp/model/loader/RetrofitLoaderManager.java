package app.dev.googlesearchapp.model.loader;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class RetrofitLoaderManager {
    public static <D> void init(final LoaderManager manager, final int loaderId,
                                final RetrofitLoader<D> loader, final Callback<D> callback) {

        manager.initLoader(loaderId, Bundle.EMPTY, new LoaderManager.LoaderCallbacks<Response<D>>() {
            @Override
            public Loader<Response<D>> onCreateLoader(int id, Bundle args) {
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Response<D>> loader, Response<D> data) {
                if (data.hasError()) {
                    callback.onFailure(data.getException());
                } else {
                    callback.onSuccess(data.getResult());
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<D>> loader) {

            }
        });
    }
}

