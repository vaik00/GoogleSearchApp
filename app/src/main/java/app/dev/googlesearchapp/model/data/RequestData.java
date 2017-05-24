package app.dev.googlesearchapp.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class RequestData {
    @SerializedName("startIndex")
    int startIndex;

    public int getStartIndex() {
        return startIndex;
    }
}
