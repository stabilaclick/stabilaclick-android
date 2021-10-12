package com.devband.stabilawalletforandroid.ui.importkey;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ImportPrivateKeyActivityModule {

    @Binds
    public abstract ImportPrivateKeyView view(ImportPrivateKeyActivity importPrivateKeyActivity);

    @Provides
    static ImportPrivateKeyPresenter provideImportPrivateKeyPresenter(ImportPrivateKeyView importPrivateKeyView,
                                                                      Stabila tron, RxJavaSchedulers rxJavaSchedulers) {
        return new ImportPrivateKeyPresenter(importPrivateKeyView, tron, rxJavaSchedulers);
    }
}
