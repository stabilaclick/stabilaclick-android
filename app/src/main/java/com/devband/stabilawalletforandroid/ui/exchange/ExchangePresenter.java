package com.devband.stabilawalletforandroid.ui.exchange;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

public class ExchangePresenter extends BasePresenter<ExchangeView> {

    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;

    public ExchangePresenter(ExchangeView view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
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
