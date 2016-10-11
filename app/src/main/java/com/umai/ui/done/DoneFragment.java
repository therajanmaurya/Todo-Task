package com.umai.ui.done;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umai.R;
import com.umai.data.model.Data;
import com.umai.ui.UpdateTaskState;
import com.umai.ui.UpdateTasks;
import com.umai.ui.adapter.TaskAdapter;
import com.umai.ui.base.BaseActivity;
import com.umai.utils.Constant;
import com.umai.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class DoneFragment extends Fragment implements DoneMvpView, UpdateTasks,
        RecyclerItemClickListener.OnItemClickListener {

    @BindView(R.id.rv_todo)
    RecyclerView rv_todo;

    @Inject
    TaskAdapter mTaskAdapter;

    @Inject
    DonePresenter mDonePresenter;

    View rootView;
    private List<Data> doneTasks;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

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
        actionModeCallback = new ActionModeCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ButterKnife.bind(this, rootView);
        mDonePresenter.attachView(this);

        if (getArguments() != null) {
            doneTasks = getArguments().getParcelableArrayList(Constant.DONE_TASK);
        }

        showUserInterface();

        return rootView;
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_todo.setLayoutManager(layoutManager);
        rv_todo.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rv_todo.setHasFixedSize(true);
        mTaskAdapter.setTask(doneTasks);
        rv_todo.setAdapter(mTaskAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDonePresenter.detachView();
        //As the Fragment Detach Finish the ActionMode
        if (actionMode != null) actionMode.finish();
    }

    @Override
    public void addTask(Data task) {
        doneTasks.add(task);
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeTask(Data task) {
        doneTasks.remove(task);
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View childView, int position) {
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            doneTasks.get(position).setState(0);
            final Data task = doneTasks.get(position);
            ((UpdateTaskState) getActivity()).changeTaskState(task);
            removeTask(task);

            //Undo task
            final Snackbar snackbar = Snackbar.make(rootView, getString(R.string.undo_task), 5000);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                    .snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((UpdateTaskState) getActivity()).undoTaskState(task);
                    task.setState(1);
                    addTask(task);
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        if (actionMode == null) {
            actionMode = ((BaseActivity) getActivity()).startSupportActionMode
                    (actionModeCallback);
        }
        toggleSelection(position);
    }

    /**
     * Toggle the selection state of an item.
     * <p>
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        mTaskAdapter.toggleSelection(position);
        int count = mTaskAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    /**
     * This ActionModeCallBack Class handling the User Event after the Selection of Clients. Like
     * Click of Menu Sync Button and finish the ActionMode
     */
    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String LOG_TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    List<Data> removeTasks = new ArrayList<>();
                    for (Integer position : mTaskAdapter.getSelectedItems()) {
                        removeTasks.add(doneTasks.get(position));
                    }
                    doneTasks.removeAll(removeTasks);
                    mTaskAdapter.notifyDataSetChanged();
                    mode.finish();

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mTaskAdapter.clearSelection();
            actionMode = null;
        }
    }
}
