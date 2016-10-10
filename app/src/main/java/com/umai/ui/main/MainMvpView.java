package com.umai.ui.main;

import com.umai.data.model.Data;
import com.umai.ui.base.MvpView;

import java.util.List;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public interface MainMvpView extends MvpView {

    void showTodos(List<Data> pendingTasks, List<Data> doneTasks);

    void showError();

    void showProgressBar(Boolean show);
}
