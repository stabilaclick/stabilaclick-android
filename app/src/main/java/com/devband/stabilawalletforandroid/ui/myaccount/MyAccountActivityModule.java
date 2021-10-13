package com.devband.stabilawalletforandroid.ui.myaccount;

import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyAccountActivityModule {

    @Binds
    public abstract MyAccountView view(MyAccountActivity myAccountActivity);

    @Provides
    static MyAccountPresenter provideMyAccountPresenter(MyAccountView myAccountView, Stabila stabila,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers, AppDatabase appDatabase) {
        return new MyAccountPresenter(myAccountView, stabila, walletAppManager, rxJavaSchedulers,
                appDatabase);
    }
}
