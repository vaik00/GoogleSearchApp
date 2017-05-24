package app.dev.googlesearchapp.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class IndexRequest {
    @SerializedName("request")
    List<RequestData> requestDatas;

    public List<RequestData> getRequestDatas() {
        return requestDatas;
    }
}
