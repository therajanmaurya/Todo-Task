package com.umai.data;

import com.umai.data.model.Todo;
import com.umai.data.remote.TodoServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Rajan Maurya on 10/10/16.
 */
@Singleton
public class DataManager {

    private final TodoServices mTodoServices;

    @Inject
    public DataManager (TodoServices todoServices) {
        mTodoServices = todoServices;
    }

    /**
     * This Method fetching the Todos
     * @return Todo
     */
    public Observable<Todo> getTodos() {
        return mTodoServices.getTodos();
    }
}
