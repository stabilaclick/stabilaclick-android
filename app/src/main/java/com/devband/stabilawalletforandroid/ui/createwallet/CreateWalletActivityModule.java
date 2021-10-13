package com.devband.stabilawalletforandroid.ui.createwallet;

import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CreateWalletActivityModule {

    @Binds
    public abstract CreateWalletView view(CreateWalletActivity createWalletActivity);

    @Provides
    static CreateWalletPresenter provideCreateWalletPresenter(CreateWalletView createWalletView,
                                                              Stabila stabila, RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference) {
        return new CreateWalletPresenter(createWalletView, stabila, rxJavaSchedulers, customPreference);
    }
}
