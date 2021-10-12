package com.devband.stabilawalletforandroid.ui.blockexplorer.account;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AccountFragmentModule {

    @Binds
    public abstract AccountView view(AccountFragment fragment);

    @Provides
    static AccountPresenter provideAccountPresenter(AccountView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new AccountPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}