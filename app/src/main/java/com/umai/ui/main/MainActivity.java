package com.umai.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umai.R;
import com.umai.data.model.Data;
import com.umai.ui.donefragment.DoneFragment;
import com.umai.ui.pendingfragment.PendingFragment;
import com.umai.ui.adapter.TodoAdapter;
import com.umai.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTodoTabs;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);

        mMainPresenter.loadTodos();
    }

    @Override
    public void onRefresh() {
        mViewPager.setVisibility(View.GONE);
        mTodoTabs.setVisibility(View.GONE);
        mMainPresenter.loadTodos();
    }

    @Override
    public void showTodos(List<Data> pendingTasks, List<Data> doneTasks) {
        mViewPager.setVisibility(View.VISIBLE);
        mTodoTabs.setVisibility(View.VISIBLE);
        TodoAdapter mTodoAdapter = new TodoAdapter(getSupportFragmentManager());
        mTodoAdapter.addFragment(PendingFragment.newInstance(pendingTasks),
                getString(R.string.pending));
        mTodoAdapter.addFragment(DoneFragment.newInstance(doneTasks), getString(R.string.done));
        mViewPager.setAdapter(mTodoAdapter);
        mTodoTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Failed to Load Todos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(Boolean show) {
        swipeRefreshLayout.setRefreshing(show);
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }
}
