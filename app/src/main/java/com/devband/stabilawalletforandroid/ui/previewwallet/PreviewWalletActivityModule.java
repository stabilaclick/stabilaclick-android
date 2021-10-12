package com.devband.stabilawalletforandroid.ui.previewwallet;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PreviewWalletActivityModule {

    @Binds
    public abstract PreviewWalletView view(PreviewWalletActivity previewWalletActivity);

    @Provides
    static PreviewWalletPresenter providePreviewWalletPresenter(PreviewWalletView previewWalletView, Stabila tron,
                                                                StabilaNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        return new PreviewWalletPresenter(previewWalletView, tron, tronNetwork, rxJavaSchedulers);
    }
}
