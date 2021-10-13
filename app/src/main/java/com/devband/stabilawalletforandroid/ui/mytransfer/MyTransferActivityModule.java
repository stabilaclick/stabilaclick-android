package com.devband.stabilawalletforandroid.ui.mytransfer;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.TokenManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyTransferActivityModule {

    @Binds
    public abstract TransferView view(TransferActivity transferActivity);

    @Provides
    static TransferPresenter provideTransferPresenter(TransferView transferView, Stabila stabila,
                                                      StabilaNetwork stabilaNetwork, TokenManager tokenManager, RxJavaSchedulers rxJavaSchedulers) {
        return new TransferPresenter(transferView, stabila, stabilaNetwork, tokenManager, rxJavaSchedulers);
    }
}
