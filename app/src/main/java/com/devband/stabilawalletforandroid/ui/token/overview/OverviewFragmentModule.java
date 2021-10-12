package com.devband.stabilawalletforandroid.ui.token.overview;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OverviewFragmentModule {

    @Binds
    public abstract OverviewView view(OverviewFragment fragment);

    @Provides
    static OverviewPresenter provideHolderPresenter(OverviewView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new OverviewPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}