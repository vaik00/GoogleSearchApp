package app.dev.googlesearchapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class QueryData implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("pagemap")
    private Pagemap pagemap;

    private String imagePath;

    private boolean selected;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public Pagemap getPagemap() {
        return pagemap;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeParcelable(this.pagemap, flags);
        dest.writeString(this.imagePath);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
    }

    public QueryData() {
    }

    protected QueryData(Parcel in) {
        this.title = in.readString();
        this.pagemap = in.readParcelable(Pagemap.class.getClassLoader());
        this.imagePath = in.readString();
        this.selected = in.readByte() != 0;
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<QueryData> CREATOR = new Parcelable.Creator<QueryData>() {
        @Override
        public QueryData createFromParcel(Parcel source) {
            return new QueryData(source);
        }

        @Override
        public QueryData[] newArray(int size) {
            return new QueryData[size];
        }
    };
}
