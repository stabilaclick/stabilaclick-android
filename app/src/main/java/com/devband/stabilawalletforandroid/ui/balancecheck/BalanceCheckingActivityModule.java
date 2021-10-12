package com.devband.stabilawalletforandroid.ui.balancecheck;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BalanceCheckingActivityModule {

    @Binds
    public abstract BalanceCheckingView view(BalanceCheckingActivity balanceCheckingActivity);

    @Provides
    static BalanceCheckingPresenter provideBalanceCheckingPresenter(BalanceCheckingView balanceCheckingView,
            RxJavaSchedulers rxJavaSchedulers) {
        return new BalanceCheckingPresenter(balanceCheckingView, rxJavaSchedulers);
    }
}
