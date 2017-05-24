package app.dev.googlesearchapp.model;


import android.content.Context;

import app.dev.googlesearchapp.model.api.ApiConstant;
import app.dev.googlesearchapp.model.api.ApiInterface;
import app.dev.googlesearchapp.model.api.ApiModule;
import app.dev.googlesearchapp.model.data.Query;
import app.dev.googlesearchapp.model.loader.RetrofitLoader;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class ModelLoader extends RetrofitLoader<Query>{
    private String q;
    private int index;

    public ModelLoader(Context ctx, String q, int index) {
        super(ctx);
        this.q = q;
        this.index = index;
    }

    @Override
    public Query call(ApiInterface service) {
        return service.getSearchData(ApiConstant.API_KEY, ApiConstant.CX, q, 10, index);
    }
}
