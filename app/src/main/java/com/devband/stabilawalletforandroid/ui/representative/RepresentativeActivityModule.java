package com.devband.stabilawalletforandroid.ui.representative;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepresentativeActivityModule {

    @Binds
    public abstract RepresentativeView view(RepresentativeActivity representativeActivity);

    @Provides
    static RepresentativePresenter provideRepresentativePresenter(RepresentativeView view, Stabila stabila,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new RepresentativePresenter(view, stabila, walletAppManager, rxJavaSchedulers);
    }
}
