package com.devband.stabilawalletforandroid.ui.sendtrc20;

import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SendTrc20ActivityModule {

    @Binds
    public abstract SendTrc20View view(SendTrc20Activity sendTrc20Activity);

    @Provides
    static SendTrc20Presenter provideRepresentativePresenter(SendTrc20View view, Stabila stabila,
            AppDatabase appDatabase, RxJavaSchedulers rxJavaSchedulers) {
        return new SendTrc20Presenter(view, stabila, appDatabase, rxJavaSchedulers);
    }
}
