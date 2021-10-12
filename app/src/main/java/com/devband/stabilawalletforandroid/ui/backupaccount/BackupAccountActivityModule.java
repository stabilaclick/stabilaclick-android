package com.devband.stabilawalletforandroid.ui.backupaccount;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BackupAccountActivityModule {

    @Binds
    public abstract BackupAccountView view(BackupAccountActivity backupAccountActivity);

    @Provides
    static BackupAccountPresenter provideBackupAccountPresenter(BackupAccountView backupAccountView,
                                                                Stabila tron, WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new BackupAccountPresenter(backupAccountView, tron, walletAppManager,
                rxJavaSchedulers);
    }
}
