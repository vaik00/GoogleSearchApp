package app.dev.googlesearchapp.view.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.model.ModelLoader;
import app.dev.googlesearchapp.model.data.Query;
import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.db.DBMethods;
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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by vaik00 on 22.05.2017.
 */
@RuntimePermissions
public class ResultFragment extends Fragment implements Callback<Query>, ResultView {
    @Bind(R.id.result_recycler) RecyclerView resultRecycler;

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void writeExternalStoragePermission(QueryData data){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPresenter.saveToDb(data, Picasso.with(getContext()));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ResultFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

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
        DBMethods db = new DBMethods(getActivity());
        mPresenter = new ResultPresenter(this, db);
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
                ResultFragmentPermissionsDispatcher.writeExternalStoragePermissionWithCheck(ResultFragment.this, data);
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
        RetrofitLoaderManager.init(getActivity().getLoaderManager(), 0, loader, this);
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
}
