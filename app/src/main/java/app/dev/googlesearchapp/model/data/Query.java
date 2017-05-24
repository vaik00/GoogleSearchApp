package app.dev.googlesearchapp.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class Query {
    @SerializedName("items")
    List<QueryData> queriesDataList;

    @SerializedName("queries")
    IndexRequest indexRequest;

    public IndexRequest getIndexRequest() {
        return indexRequest;
    }

    public List<QueryData> getQueriesDataList() {
        return queriesDataList;
    }
}
