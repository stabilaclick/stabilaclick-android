package com.devband.stabilawalletforandroid.ui.market;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MarketActivityModule {

    @Binds
    public abstract MarketView view(MarketActivity marketActivity);

    @Provides
    static MarketPresenter provideMarketPresenter(MarketView marketView, StabilaNetwork stabilaNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new MarketPresenter(marketView, stabilaNetwork, rxJavaSchedulers);
    }
}
