package com.umai.ui;

import com.umai.data.model.Data;

public interface UpdateTaskState {

    void changeTaskState(Data task);

    void undoTaskState(Data task);

}