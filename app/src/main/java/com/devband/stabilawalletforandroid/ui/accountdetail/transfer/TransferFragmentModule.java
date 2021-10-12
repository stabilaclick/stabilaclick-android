package com.devband.stabilawalletforandroid.ui.accountdetail.transfer;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TransferFragmentModule {

    @Binds
    public abstract TransferView view(TransferFragment fragment);

    @Provides
    static TransferPresenter provideTransferPresenter(TransferView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new TransferPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}