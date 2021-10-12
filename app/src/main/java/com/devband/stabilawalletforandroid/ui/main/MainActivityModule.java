package com.devband.stabilawalletforandroid.ui.main;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    @Binds
    public abstract MainView view(MainActivity mainActivity);

    @Provides
    static MainPresenter provideMainPresenter(MainView mainView, Stabila tron, WalletAppManager walletAppManager,
                                              StabilaNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference,
                                              AppDatabase appDatabase) {
        return new MainPresenter(mainView, tron, walletAppManager, tronNetwork, rxJavaSchedulers,
                customPreference, appDatabase);
    }
}
