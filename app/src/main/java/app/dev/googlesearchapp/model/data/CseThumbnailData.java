package app.dev.googlesearchapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class CseThumbnailData implements Parcelable {
    @SerializedName("src")
    private String src;

    public String getSrc() {
        return src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.src);
    }

    public CseThumbnailData() {
    }

    protected CseThumbnailData(Parcel in) {
        this.src = in.readString();
    }

    public static final Parcelable.Creator<CseThumbnailData> CREATOR = new Parcelable.Creator<CseThumbnailData>() {
        @Override
        public CseThumbnailData createFromParcel(Parcel source) {
            return new CseThumbnailData(source);
        }

        @Override
        public CseThumbnailData[] newArray(int size) {
            return new CseThumbnailData[size];
        }
    };
}
