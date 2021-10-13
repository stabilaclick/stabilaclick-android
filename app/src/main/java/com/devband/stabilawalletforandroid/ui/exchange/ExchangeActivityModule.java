package com.devband.stabilawalletforandroid.ui.exchange;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ExchangeActivityModule {

    @Binds
    public abstract ExchangeView view(ExchangeActivity exchangeActivity);

    @Provides
    static ExchangePresenter provideExchangePresenter(ExchangeView view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers) {
        return new ExchangePresenter(view, stabila, rxJavaSchedulers);
    }
}
