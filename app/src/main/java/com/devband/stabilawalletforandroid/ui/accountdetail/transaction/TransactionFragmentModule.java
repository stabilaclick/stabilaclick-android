package com.devband.stabilawalletforandroid.ui.accountdetail.transaction;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TransactionFragmentModule {

    @Binds
    public abstract TransactionView view(TransactionFragment fragment);

    @Provides
    static TransactionPresenter provideTransactionPresenter(TransactionView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new TransactionPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}