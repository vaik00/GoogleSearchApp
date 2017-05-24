package app.dev.googlesearchapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class PhotoFragment extends Fragment {
    public static final String IMAGE_PATH = "app.dev.googlesearchapp.image_src_extra";
    public static final String IMAGE_FROM_DB_PATH = "app.dev.googlesearchapp.image_db_extra";

    @Bind(R.id.image) ImageView image;

    @OnClick(R.id.btn_close)
    void closeFragment() {
        getActivity().onBackPressed();
    }

    private String mSrcImagePath;
    private String mDbImagePath;

    public static PhotoFragment newInstance(@Nullable String imagePath, @Nullable String imageDbPath) {
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH, imagePath);
        bundle.putString(IMAGE_FROM_DB_PATH, imageDbPath);
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.mSrcImagePath = args.getString(IMAGE_PATH);
        this.mDbImagePath = args.getString(IMAGE_FROM_DB_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_fullscreen, container, false);
        ButterKnife.bind(this, rootView);
        loadImage();
        return rootView;
    }

    private void loadImage() {
        if (mDbImagePath != null) {
            Picasso
                    .with(getActivity())
                    .load(FileHelper.getFile(mDbImagePath))
                    .fit()
                    .centerCrop()
                    .into(image);
        } else if (mSrcImagePath != null) {
            Picasso
                    .with(getActivity())
                    .load(mSrcImagePath)
                    .fit()
                    .centerCrop()
                    .into(image);
        } else {
            image.setImageResource(R.drawable.ic_placeholder);
        }
    }
}
