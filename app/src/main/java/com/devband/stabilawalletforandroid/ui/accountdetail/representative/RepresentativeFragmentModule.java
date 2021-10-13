package com.devband.stabilawalletforandroid.ui.accountdetail.representative;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepresentativeFragmentModule {

    @Binds
    public abstract RepresentativeView view(RepresentativeFragment fragment);

    @Provides
    static RepresentativePresenter provideRepresentativePresenter(RepresentativeView view, StabilaNetwork stabilaNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new RepresentativePresenter(view, stabilaNetwork, rxJavaSchedulers);
    }
}