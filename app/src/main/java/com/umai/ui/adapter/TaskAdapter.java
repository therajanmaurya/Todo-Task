package com.umai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.umai.R;
import com.umai.data.model.Data;
import com.umai.ui.base.SelectableAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends SelectableAdapter<TaskAdapter.ViewHolder> {

    private List<Data> tasks;

    @Inject
    public TaskAdapter() {
        tasks = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data task = tasks.get(position);

        holder.task.setText(task.getName());
        if (task.getState() == 0) {
            holder.task_state.setChecked(false);
        } else {
            holder.task_state.setChecked(true);
        }
    }

    public void removeTask(int position) {
        tasks.remove(position);
        notifyDataSetChanged();
    }

    public void changeState(int position) {
        int toggle =  tasks.get(position).getState();
        if (toggle == 1) {
            tasks.get(position).setState(0);
        } else {
            tasks.get(position).setState(1);
        }
        notifyItemChanged(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTask(List<Data> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.task)
        TextView task;

        @BindView(R.id.task_state)
        CheckBox task_state;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}