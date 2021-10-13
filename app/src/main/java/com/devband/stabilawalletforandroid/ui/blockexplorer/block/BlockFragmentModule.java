package com.devband.stabilawalletforandroid.ui.blockexplorer.block;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BlockFragmentModule {

    @Binds
    public abstract BlockView view(BlockFragment fragment);

    @Provides
    static BlockPresenter provideBlockPresenter(BlockView view, StabilaNetwork stabilaNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new BlockPresenter(view, stabilaNetwork, rxJavaSchedulers);
    }
}