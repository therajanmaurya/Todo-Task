package com.umai.ui.main;

import com.umai.data.DataManager;
import com.umai.data.model.Data;
import com.umai.data.model.Todo;
import com.umai.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.clear();
    }

    public void loadTodos() {
        checkViewAttached();
        getMvpView().showProgressBar(true);
        mSubscriptions.add(mDataManager.getTodos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Todo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressBar(false);
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Todo todo) {
                        getMvpView().showProgressBar(false);
                        getMvpView().showTodos(filterTasks(todo, 0), filterTasks(todo, 1));
                    }
                })
        );
    }

    private List<Data> filterTasks(Todo todo, final Integer state)  {
        final List<Data> tasks = new ArrayList<>();
        Observable.from(todo.getData())
                .filter(new Func1<Data, Boolean>() {
                    @Override
                    public Boolean call(Data data) {
                        return (data.getState() == state);
                    }
                })
                .subscribe(new Action1<Data>() {
                    @Override
                    public void call(Data data) {
                        tasks.add(data);
                    }
                });
        return tasks;
    }

}
