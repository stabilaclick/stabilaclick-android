package com.devband.stabilawalletforandroid.ui.sendtrc10;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SendTrc10ActivityModule {

    @Binds
    public abstract SendTrc10View view(SendTrc10Activity sendTrc10Activity);

    @Provides
    static SendTrc10Presenter provideRepresentativePresenter(SendTrc10View view, Stabila stabila,
            RxJavaSchedulers rxJavaSchedulers) {
        return new SendTrc10Presenter(view, stabila, rxJavaSchedulers);
    }
}
