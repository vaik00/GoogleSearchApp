package app.dev.googlesearchapp.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.model.ModelLoader;
import app.dev.googlesearchapp.model.data.Query;
import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.db.QueryType;
import app.dev.googlesearchapp.model.db.TaskListener;
import app.dev.googlesearchapp.model.loader.Callback;
import app.dev.googlesearchapp.model.loader.RetrofitLoaderManager;
import app.dev.googlesearchapp.presenter.ResultPresenter;
import app.dev.googlesearchapp.utils.Constants;
import app.dev.googlesearchapp.view.SnackbarHelper;
import app.dev.googlesearchapp.view.adapter.ActionListener;
import app.dev.googlesearchapp.view.adapter.EndlessRecyclerViewScrollListener;
import app.dev.googlesearchapp.view.adapter.ResultAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vaik00 on 22.05.2017.
 */
public class ResultFragment extends Fragment implements Callback<Query>, ResultView, TaskListener {
    @Bind(R.id.result_recycler)
    RecyclerView resultRecycler;

    private ResultPresenter mPresenter;
    private String mSearchQuery;
    private ResultAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, v);
        mPresenter = new ResultPresenter(this, getContext(), this);
        loadUI(v);
        return v;

    }

    @Override
    public void onFailure(Exception ex) {
        SnackbarHelper.showErrorSnackbar(getActivity(), ex.getMessage());
    }

    @Override
    public void onSuccess(Query result) {
        mPresenter.loadData(result);
        getActivity().getLoaderManager().destroyLoader(0);
    }

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                QueryData data = intent.getParcelableExtra(Constants.UPDATE_ITEM_INTENT);
                mPresenter.updateItem(mAdapter.getDataList(), data);
            }
        }
    };

    @Override
    public void updateItem(int position, QueryData data) {
        mAdapter.updateData(position, data);
    }

    @Override
    public void loadUI(View v) {
        mAdapter = new ResultAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        resultRecycler.setLayoutManager(linearLayoutManager);
        resultRecycler.setItemAnimator(new DefaultItemAnimator());
        resultRecycler.setNestedScrollingEnabled(false);
        resultRecycler.setAdapter(mAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                loadMore(mAdapter.getItemCount());
            }
        };
        mAdapter.setOnActionListener(new ActionListener() {
            @Override
            public void openImage(String path) {
                openImageFragment(path);
            }

            @Override
            public void saveToDB(QueryData data) {
                String imageName = Long.toString(System.currentTimeMillis()) + ".jpeg";
                String src = data.getPagemap().getCseThumbnailData().get(0).getSrc();
                if(checkWriteExternalPermission()) {
                    mPresenter.saveToDb(data, imageName);
                    mPresenter.saveImage(src, imageName, Picasso.with(getContext()));
                }else{
                    mPresenter.saveToDb(data, data.getPagemap().getCseThumbnailData().get(0).getSrc());
                }
            }

            @Override
            public void deleteFromDB(int id, int position, String imageName) {
                mPresenter.deleteFromDb(id, imageName);
            }
        });
        resultRecycler.addOnScrollListener(scrollListener);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(
                        mUpdateReceiver,
                        new IntentFilter(
                                Constants.UPDATE_ITEM_INTENT
                        )
                );
    }

    private boolean checkWriteExternalPermission() {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void openImageFragment(String path) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PhotoFragment fragment = PhotoFragment.newInstance(path, null);
        transaction.replace(R.id.container, fragment, "photo");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void loadData(String query, int index) {
        ModelLoader loader = new ModelLoader(getActivity(), query, index);
        RetrofitLoaderManager.init(getActivity().getSupportLoaderManager(), 0, loader, this);
    }

    @Override
    public void showData(List<QueryData> data) {
        mAdapter.setData(data);
    }

    @Override
    public void addData(List<QueryData> data) {
        scrollListener.reset(mAdapter.getItemCount(), false);
        mAdapter.addData(data);
    }

    @Override
    public void loadMore(int index) {
        loadData(mSearchQuery, index);
    }

    @Override
    public void update(String searchQuery) {
        mSearchQuery = searchQuery;
        loadData(searchQuery, 1);
    }

    @Override
    public void showSuccessSaveSnackbar() {
        SnackbarHelper.showSuccessSnackbar(getActivity(), getResources().getString(R.string.add_to_favorite));
    }

    @Override
    public void showSuccessDeleteSnackbar() {
        SnackbarHelper.showErrorSnackbar(getActivity(), getResources().getString(R.string.delete_from_favorite));
    }

    @Override
    public void onTaskCompleted(QueryType queryType, Object result) {
        switch (queryType) {
            case INSERT:
                Log.d("INSERT", "row ID of the newly inserted row = " + (Long) result + "");
                showSuccessSaveSnackbar();
                break;
            case DELETE:
                Log.d("DELETE", "REMOVED");
                showSuccessDeleteSnackbar();
                break;
        }
    }
}
