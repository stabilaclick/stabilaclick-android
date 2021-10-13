package com.devband.stabilawalletforandroid.ui.token.holder;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HolderFragmentModule {

    @Binds
    public abstract HolderView view(HolderFragment fragment);

    @Provides
    static HolderPresenter provideHolderPresenter(HolderView view, StabilaNetwork stabilaNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new HolderPresenter(view, stabilaNetwork, rxJavaSchedulers);
    }
}