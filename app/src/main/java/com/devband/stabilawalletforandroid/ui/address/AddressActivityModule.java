package com.devband.stabilawalletforandroid.ui.address;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AddressActivityModule {

    @Binds
    public abstract AddressView view(AddressActivity addressActivity);

    @Provides
    static AddressPresenter provideAddressPresenter(AddressView addressView, Stabila tron,
            RxJavaSchedulers rxJavaSchedulers) {
        return new AddressPresenter(addressView, tron, rxJavaSchedulers);
    }
}
