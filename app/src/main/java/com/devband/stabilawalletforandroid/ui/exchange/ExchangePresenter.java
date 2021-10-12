package com.devband.stabilawalletforandroid.ui.exchange;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

public class ExchangePresenter extends BasePresenter<ExchangeView> {

    private Stabila mTron;
    private RxJavaSchedulers mRxJavaSchedulers;

    public ExchangePresenter(ExchangeView view, Stabila tron, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
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
