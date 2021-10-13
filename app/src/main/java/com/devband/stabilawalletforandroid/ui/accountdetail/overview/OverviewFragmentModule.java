package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OverviewFragmentModule {

    @Binds
    public abstract OverviewView view(OverviewFragment fragment);

    @Provides
    static OverviewPresenter provideOverviewPresenter(OverviewView view, Stabila stabila, StabilaNetwork stabilaNetwork,
                                                      RxJavaSchedulers rxJavaSchedulers) {
        return new OverviewPresenter(view, stabila, stabilaNetwork, rxJavaSchedulers);
    }
}