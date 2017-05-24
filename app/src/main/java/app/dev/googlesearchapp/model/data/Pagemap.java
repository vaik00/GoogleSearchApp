package app.dev.googlesearchapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class Pagemap implements Parcelable {
    @SerializedName("cse_thumbnail")
    private List<CseThumbnailData> cseThumbnailData;

    public List<CseThumbnailData> getCseThumbnailData() {
        return cseThumbnailData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.cseThumbnailData);
    }

    public Pagemap() {
    }

    protected Pagemap(Parcel in) {
        this.cseThumbnailData = new ArrayList<CseThumbnailData>();
        in.readList(this.cseThumbnailData, CseThumbnailData.class.getClassLoader());
    }

    public static final Parcelable.Creator<Pagemap> CREATOR = new Parcelable.Creator<Pagemap>() {
        @Override
        public Pagemap createFromParcel(Parcel source) {
            return new Pagemap(source);
        }

        @Override
        public Pagemap[] newArray(int size) {
            return new Pagemap[size];
        }
    };
}
