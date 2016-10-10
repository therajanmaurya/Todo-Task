package com.umai.ui.donefragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umai.R;
import com.umai.data.model.Data;
import com.umai.ui.adapter.TaskAdapter;
import com.umai.ui.base.BaseActivity;
import com.umai.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class DoneFragment extends Fragment implements DoneMvpView {

    @BindView(R.id.rv_todo)
    RecyclerView rv_todo;

    @Inject
    TaskAdapter mTaskAdapter;

    @Inject
    DonePresenter mDonePresenter;

    View rootView;
    private List<Data> doneTasks;

    public static DoneFragment newInstance(List<Data> todoDatas) {
        Bundle arguments = new Bundle();
        DoneFragment doneFragment = new DoneFragment();
        arguments.putParcelableArrayList(Constant.DONE_TASK,
                (ArrayList<? extends Parcelable>) todoDatas);
        doneFragment.setArguments(arguments);
        return doneFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        if (getArguments() != null) {
            doneTasks = getArguments().getParcelableArrayList(Constant.DONE_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this, rootView);
        mDonePresenter.attachView(this);
        showUserInterface();

        return rootView;
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_todo.setLayoutManager(layoutManager);
        rv_todo.setHasFixedSize(true);
        mTaskAdapter.setTask(doneTasks);
        rv_todo.setAdapter(mTaskAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDonePresenter.detachView();
    }
}
