package app.dev.googlesearchapp.model.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import app.dev.googlesearchapp.model.api.ApiInterface;
import app.dev.googlesearchapp.model.api.ApiModule;

/**
 * Created by vaik00 on 22.05.2017.
 */

public abstract class RetrofitLoader<D> extends AsyncTaskLoader<Response<D>> {

    private final ApiInterface mService = ApiModule.getService();

    private Response<D> mCachedResponse;

    public RetrofitLoader(Context context) {

        super(context);
    }

    @Override
    public Response<D> loadInBackground() {

        try {

            final D data = call(mService);
            mCachedResponse = Response.ok(data);

        } catch (Exception ex) {

            mCachedResponse = Response.error(ex);
        }

        return mCachedResponse;
    }

    @Override
    protected void onStartLoading() {

        super.onStartLoading();

        if (mCachedResponse != null) {

            deliverResult(mCachedResponse);
        }

        if (takeContentChanged() || mCachedResponse == null) {

            forceLoad();
        }
    }

    @Override
    protected void onReset() {

        super.onReset();

        mCachedResponse = null;
    }

    public abstract D call(ApiInterface service);
}