package com.devband.stabilawalletforandroid.ui.login;

import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginActivityModule {

    @Binds
    public abstract LoginView view(LoginActivity loginActivity);

    @Provides
    static LoginPresenter provideLoginPresenter(LoginView loginView, Stabila stabila,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers,
            CustomPreference customPreference) {
        return new LoginPresenter(loginView, stabila, walletAppManager, rxJavaSchedulers, customPreference);
    }
}
