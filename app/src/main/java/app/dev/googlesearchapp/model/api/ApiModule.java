package app.dev.googlesearchapp.model.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class ApiModule {
    private static ApiInterface service;
    private static final String BASE_URL = "https://www.googleapis.com/customsearch/";

    public static ApiInterface getService() {
        if (service == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ApiInterface.class);
            return service;
        } else {
            return service;
        }
    }
}
