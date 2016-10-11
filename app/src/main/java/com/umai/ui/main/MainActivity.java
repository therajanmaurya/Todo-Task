package com.umai.ui.main;

import static com.umai.R.string.state;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.umai.R;
import com.umai.data.model.Data;
import com.umai.ui.UpdateTaskState;
import com.umai.ui.UpdateTasks;
import com.umai.ui.adapter.TodoAdapter;
import com.umai.ui.base.BaseActivity;
import com.umai.ui.done.DoneFragment;
import com.umai.ui.pending.PendingFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainMvpView,
        SwipeRefreshLayout.OnRefreshListener, UpdateTaskState {

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

    @OnClick(R.id.fab_add_task)
    void onClickAddTask() {
        final boolean[] taskState = new boolean[1];
        new MaterialDialog.Builder(this)
                .title(R.string.add_task)
                .content(R.string.what_is_task)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 50)
                .positiveText(R.string.add_task)
                .checkBoxPromptRes(state, false,
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                taskState[0] = isChecked;
                            }
                        })
                .input(null, null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Data task = new Data();
                        task.setName(input.toString());
                        if (taskState[0]) {
                            task.setState(1);
                            ((UpdateTasks) getSupportFragmentManager()
                                    .findFragmentByTag(getFragmentTag(1))).addTask(task);
                            //mViewPager.setCurrentItem(1, true);
                        } else {
                            task.setState(0);
                            ((UpdateTasks) getSupportFragmentManager()
                                    .findFragmentByTag(getFragmentTag(0))).addTask(task);
                            //mViewPager.setCurrentItem(0, true);
                        }
                    }
                })
                .show();
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewpager + ":" + position;
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

    @Override
    public void changeTaskState(Data task) {
        if (task.getState() == 1) {
            ((UpdateTasks) getSupportFragmentManager()
                    .findFragmentByTag(getFragmentTag(1))).addTask(task);
        } else {
            ((UpdateTasks) getSupportFragmentManager()
                    .findFragmentByTag(getFragmentTag(0))).addTask(task);
        }
    }
}
