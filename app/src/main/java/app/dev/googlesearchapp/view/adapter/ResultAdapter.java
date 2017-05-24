package app.dev.googlesearchapp.view.adapter;

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

import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.model.data.QueryData;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vaik00 on 23.05.2017.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private static ActionListener sActionListener;

    private List<QueryData> mQueryData;

    public ResultAdapter() {
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

    public void updateData(int position, QueryData data) {
        mQueryData.set(position, data);
        notifyItemChanged(position);
    }

    public List<QueryData> getDataList() {
        return mQueryData;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        QueryData data = mQueryData.get(position);
        holder.checkBox.setChecked(data.isSelected());
        holder.textView.setText(data.getTitle());
        if (isDataHasSrcPath(data)) {
            String src = data.getPagemap().getCseThumbnailData().get(0).getSrc();
            Picasso
                    .with(holder.itemView.getContext())
                    .load(src)
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_placeholder);
        }
    }

    private boolean isDataHasSrcPath(QueryData data) {
        return data.getPagemap() != null && data.getPagemap().getCseThumbnailData() != null;
    }

    @Override
    public int getItemCount() {
        return mQueryData.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.text) TextView textView;
        @Bind(R.id.checkbox) CheckBox checkBox;

        ResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(view -> {
                if (sActionListener != null) {
                    String path = null;
                    if (isDataHasSrcPath(mQueryData.get(getLayoutPosition())))
                        path = mQueryData.get(getLayoutPosition()).getPagemap().getCseThumbnailData().get(0).getSrc();
                    sActionListener.openImage(path);
                }
            });
            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (compoundButton.isPressed()) {
                    QueryData data = mQueryData.get(getLayoutPosition());
                    data.setSelected(isChecked);
                    if (isChecked) {
                        sActionListener.saveToDB(data);
                    } else {
                        sActionListener.deleteFromDB(data.getId(), getLayoutPosition(), data.getImagePath());
                    }
                }
            });
        }
    }
}
