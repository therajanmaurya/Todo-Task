package com.umai.ui.pendingfragment;

import com.umai.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class PendingPresenter extends BasePresenter<PendingMvpView> {


    @Inject
    public PendingPresenter() {

    }

    @Override
    public void attachView(PendingMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
