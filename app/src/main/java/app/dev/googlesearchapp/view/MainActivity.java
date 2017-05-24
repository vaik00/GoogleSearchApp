package app.dev.googlesearchapp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import app.dev.googlesearchapp.R;
import app.dev.googlesearchapp.utils.RxSearch;
import app.dev.googlesearchapp.view.adapter.SearchPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import rx.android.schedulers.AndroidSchedulers;

import com.lapism.searchview.SearchView;

import java.util.concurrent.TimeUnit;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class MainActivity extends BaseActivity implements MainView {
    public static final String TAG = "SEARCH_ACTIVITY";
    public static final String SAVED_QUERY = "app.dev.googlesearchapp.saved_query";

    @Bind(R.id.search_view)
    SearchView searchView;
    @Bind(R.id.pager)
    ViewPager viewPager;

    private String mQuery;

    @OnPageChange(R.id.pager)
    void onPageSelected(int position) {
        tabPositionSelected = position;
        refreshTab(tabPositionSelected, "");
    }

    private SearchPagerAdapter adapter;
    private int tabPositionSelected = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        loadUI();
        loadSearchView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current query state
        savedInstanceState.putString(SAVED_QUERY, mQuery);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        mQuery = savedInstanceState.getString(SAVED_QUERY);
    }

    @Override
    public void loadUI() {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        adapter = new SearchPagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void loadSearchView() {
        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(item -> item.length() >= 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    mQuery = query;
                    adapter.refreshTab(tabPositionSelected, query);
                }, error -> Log.e(TAG, error.getMessage()));
    }

    private void refreshTab(int position, String textSearch) {
        adapter.refreshTab(position, textSearch);
    }
}
