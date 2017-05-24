package app.dev.googlesearchapp.view.fragment;

import android.Manifest;
import android.content.Intent;
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

import java.util.List;

import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.model.data.QueryData;
import app.dev.googlesearchapp.model.db.DBMethods;
import app.dev.googlesearchapp.presenter.FavouritePresenter;
import app.dev.googlesearchapp.utils.Constants;
import app.dev.googlesearchapp.view.SnackbarHelper;
import app.dev.googlesearchapp.view.adapter.ActionListener;
import app.dev.googlesearchapp.view.adapter.EndlessRecyclerViewScrollListener;
import app.dev.googlesearchapp.view.adapter.FavouriteAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by vaik00 on 22.05.2017.
 */

@RuntimePermissions
public class FavouriteFragment extends Fragment  implements FavouriteView{
    @Bind(R.id.result_recycler) RecyclerView resultRecycler;

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void writeExternalStoragePermission(int id, int position, String imageName){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPresenter.deleteFromDb(id, position, imageName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        FavouriteFragmentPermissionsDispatcher
                .onRequestPermissionsResult(
                        this,
                        requestCode,
                        grantResults);
    }

    private FavouritePresenter mPresenter;
    private FavouriteAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        DBMethods db = new DBMethods(getActivity());
        mPresenter = new FavouritePresenter(this, db);
        loadUI(v);
        loadData(1);
        return v;

    }

    @Override
    public void loadUI(View v) {
        ButterKnife.bind(this, v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new FavouriteAdapter();
        resultRecycler.setLayoutManager(linearLayoutManager);
        resultRecycler.setItemAnimator(new DefaultItemAnimator());
        resultRecycler.setNestedScrollingEnabled(false);
        resultRecycler.setAdapter(mAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                loadData(mAdapter.getItemCount());
            }
        };
        mAdapter.setOnActionListener(new ActionListener() {
            @Override
            public void openImage(String path) {
                openImageFragment(path);
            }

            @Override
            public void saveToDB(QueryData data) {

            }

            @Override
            public void deleteFromDB(int id, int position, String imageName) {
                FavouriteFragmentPermissionsDispatcher
                        .writeExternalStoragePermissionWithCheck(
                                FavouriteFragment.this,
                                id,
                                position,
                                imageName);
            }
        });
    }

    @Override
    public void openImageFragment(String path) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PhotoFragment fragment = PhotoFragment.newInstance(null, path);
        transaction.replace(R.id.container, fragment, "photo");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void loadData(int index) {
        mPresenter.loadData(index);
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
    public void deleteItem(int position) {
        Intent updateIntent = new Intent(Constants.UPDATE_ITEM_INTENT);
        QueryData data = mAdapter.getItemAtPosition(position);
        data.setSelected(false);
        updateIntent.putExtra(Constants.UPDATE_ITEM_INTENT, data);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(updateIntent);
        mAdapter.removeItem(position);
    }

    @Override
    public void update(String searchQuery) {
        mPresenter.loadData(0);
    }


    @Override
    public void showSuccessDeleteSnackbar() {
        SnackbarHelper.showErrorSnackbar(getActivity(), getResources().getString(R.string.delete_from_favorite));
    }
}
