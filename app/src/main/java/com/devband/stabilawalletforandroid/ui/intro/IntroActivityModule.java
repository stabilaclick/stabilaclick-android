package com.devband.stabilawalletforandroid.ui.intro;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class IntroActivityModule {

    @Binds
    public abstract IntroView view(IntroActivity introActivity);

    @Provides
    static IntroPresenter provideIntroPresenter(IntroView introView, Stabila stabila,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new IntroPresenter(introView, stabila, walletAppManager, rxJavaSchedulers);
    }
}
