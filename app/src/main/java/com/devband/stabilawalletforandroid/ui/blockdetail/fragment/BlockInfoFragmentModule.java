package com.devband.stabilawalletforandroid.ui.blockdetail.fragment;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BlockInfoFragmentModule {

    @Binds
    public abstract BlockInfoView view(BlockInfoFragment fragment);

    @Provides
    static BlockInfoPresenter provideBlockInfoPresenter(BlockInfoView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new BlockInfoPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}