package app.dev.googlesearchapp.model.api;

import app.dev.googlesearchapp.model.data.Query;
import retrofit2.http.GET;

/**
 * Created by vaik00 on 22.05.2017.
 */

public interface ApiInterface {
    @GET("v1")
    Query getSearchData(
            @retrofit2.http.Query("key") String key,
            @retrofit2.http.Query("cx") String cx,
            @retrofit2.http.Query("q") String q,
            @retrofit2.http.Query("num") int num,
            @retrofit2.http.Query("start") int start);
}
