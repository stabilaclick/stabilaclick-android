package com.devband.stabilawalletforandroid.ui.token;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TokenActivityModule {

    @Binds
    public abstract TokenView view(TokenActivity tokenActivity);

    @Provides
    static TokenPresenter provideTokenPresenter(TokenView view, Stabila stabila, StabilaNetwork stabilaNetwork,
                                                WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new TokenPresenter(view, stabila, stabilaNetwork, walletAppManager, rxJavaSchedulers);
    }
}
