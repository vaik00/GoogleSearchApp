package app.dev.googlesearchapp.view.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.dev.googlesearchapp.utils.FileHelper;
import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.model.data.QueryData;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vaik00 on 24.05.2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private static ActionListener sActionListener;

    private List<QueryData> mQueryData;

    public FavouriteAdapter() {
        mQueryData = new ArrayList<>();
    }

    public void setOnActionListener(ActionListener listener) {
        sActionListener = listener;
    }

    public void setData(List<QueryData> data) {
        if (data.size() != 0) {
            mQueryData.clear();
            mQueryData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(List<QueryData> data) {
        mQueryData.addAll(data);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mQueryData.remove(position);
        notifyItemRemoved(position);
    }

    public QueryData getItemAtPosition(int position){
        return mQueryData.get(position);
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new FavouriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        QueryData data = mQueryData.get(position);
        holder.textView.setText(data.getTitle());
        if(checkWriteExternalPermission(holder.itemView.getContext())){
            Picasso
                    .with(holder.itemView.getContext())
                    .load(FileHelper.getFile(data.getImagePath()))
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        }else{
            Picasso
                    .with(holder.itemView.getContext())
                    .load(data.getImagePath())
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        }

        holder.checkBox.setChecked(data.isSelected());
    }

    private boolean checkWriteExternalPermission(Context context) {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public int getItemCount() {
        return mQueryData.size();
    }


    class FavouriteViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.text) TextView textView;
        @Bind(R.id.checkbox) CheckBox checkBox;

        FavouriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(view -> {
                if (sActionListener != null)
                    sActionListener.openImage(mQueryData.get(getLayoutPosition()).getImagePath());
            });
            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (compoundButton.isPressed()) {
                    QueryData data = mQueryData.get(getLayoutPosition());
                    data.setSelected(isChecked);
                    if (!isChecked)
                        sActionListener.deleteFromDB(data.getId(), getLayoutPosition(), data.getImagePath());
                }
            });
        }
    }
}
