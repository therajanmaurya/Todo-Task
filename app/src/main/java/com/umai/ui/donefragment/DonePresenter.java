package com.umai.ui.donefragment;

import com.umai.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class DonePresenter extends BasePresenter<DoneMvpView> {

    @Inject
    public DonePresenter() {

    }

    @Override
    public void attachView(DoneMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
