package com.devband.stabilawalletforandroid.ui.balancecheck;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

public class BalanceCheckingPresenter extends BasePresenter<BalanceCheckingView> {

    private RxJavaSchedulers mRxJavaSchedulers;

    public BalanceCheckingPresenter(BalanceCheckingView view, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
