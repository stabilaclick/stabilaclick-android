package com.devband.stabilawalletforandroid.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Build;

import com.devband.stabilalib.Hosts;
import com.devband.stabilalib.ServiceBuilder;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.services.AccountService;
import com.devband.stabilalib.services.CoinMarketCapService;
import com.devband.stabilalib.services.TokenService;
import com.devband.stabilalib.services.StabilaGridService;
import com.devband.stabilalib.services.StabilaScanService;
import com.devband.stabilalib.services.VoteService;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.common.security.PasswordEncoder;
import com.devband.stabilawalletforandroid.common.security.PasswordEncoderImpl;
import com.devband.stabilawalletforandroid.common.security.UpdatableBCrypt;
import com.devband.stabilawalletforandroid.common.security.keystore.KeyStore;
import com.devband.stabilawalletforandroid.common.security.keystore.KeyStoreApi15Impl;
import com.devband.stabilawalletforandroid.common.security.keystore.KeyStoreApi18Impl;
import com.devband.stabilawalletforandroid.common.security.keystore.KeyStoreApi23Impl;
import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.di.ApplicationContext;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulersImpl;
import com.devband.stabilawalletforandroid.stabila.AccountManager;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.TokenManager;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;
import com.devband.stabilawalletforandroid.stabila.repository.LocalDbRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Binds
    @ApplicationContext
    abstract Context bindContext(Application application);

    @Provides
    @Singleton
    static CustomPreference provideCustomPreference(@ApplicationContext Context context) {
        return  new CustomPreference(context);
    }

    @Provides
    @Singleton
    static VoteService provideVoteService() {
        return ServiceBuilder.createService(VoteService.class, Hosts.STABILASCAN_API_LIST);
    }

    @Provides
    @Singleton
    static CoinMarketCapService provideCoinMarketCapService() {
        return ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
    }

    @Provides
    @Singleton
    static StabilaScanService provideTronScanService() {
        return ServiceBuilder.createService(StabilaScanService.class, Hosts.STABILASCAN_API_LIST);
    }

    @Provides
    @Singleton
    static TokenService provideTokenService() {
        return ServiceBuilder.createService(TokenService.class, Hosts.STABILASCAN_API_LIST);
    }

    @Provides
    @Singleton
    static AccountService provideAccountService() {
        return ServiceBuilder.createService(AccountService.class, Hosts.STABILASCAN_API_LIST);
    }

    @Provides
    @Singleton
    static StabilaGridService provideTronGridService() {
        return ServiceBuilder.createService(StabilaGridService.class, Hosts.STABILAGRID_API);
    }

    @Provides
    @Singleton
    static StabilaNetwork provideTronNetwork(VoteService voteService, CoinMarketCapService coinMarketCapService,
                                             StabilaScanService tronScanService, TokenService tokenService, AccountService accountService) {
        return new StabilaNetwork(voteService, coinMarketCapService, tronScanService,
                tokenService, accountService);
    }

    @Provides
    @Singleton
    static AccountManager provideAccountManager(AppDatabase appDatabase, KeyStore keyStore) {
        return new AccountManager(new LocalDbRepository(appDatabase), keyStore);
    }

    @Provides
    @Singleton
    static KeyStore provideKeyStore(@ApplicationContext Context context, CustomPreference customPreference) {
        KeyStore keyStore = null;

        int keyStoreVersion = customPreference.getInitWallet() ? customPreference.getKeyStoreVersion() : Build.VERSION.SDK_INT;

        if (keyStoreVersion >= Build.VERSION_CODES.M) {
            keyStore = new KeyStoreApi23Impl(customPreference);
        } else if (keyStoreVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            keyStore = new KeyStoreApi18Impl(context);
        } else {
            keyStore = new KeyStoreApi15Impl(customPreference);
        }

        keyStore.init();
        keyStore.createKeys(Constants.ALIAS_SALT);
        keyStore.createKeys(Constants.ALIAS_ACCOUNT_KEY);
        keyStore.createKeys(Constants.ALIAS_PASSWORD_KEY);
        keyStore.createKeys(Constants.ALIAS_ADDRESS_KEY);

        return keyStore;
    }

    @Provides
    @Singleton
    static UpdatableBCrypt provideUpdatableBCrypt() {
        return new UpdatableBCrypt(Constants.SALT_LOG_ROUND);
    }

    @Provides
    @Singleton
    static PasswordEncoder providePasswordEncoder(CustomPreference customPreference, KeyStore keyStore,
            UpdatableBCrypt updatableBCrypt) {
        PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl(customPreference, keyStore, updatableBCrypt);
        passwordEncoder.init();

        return passwordEncoder;
    }

    @Provides
    @Singleton
    static WalletAppManager provideWalletAppManager(PasswordEncoder passwordEncoder, AppDatabase appDatabase) {
        return new WalletAppManager(passwordEncoder, appDatabase);
    }

    @Provides
    @Singleton
    static TokenManager provideTokenManager(AppDatabase appDatabase) {
        return new TokenManager(appDatabase.trc10AssetDao());
    }

    @Provides
    @Singleton
    static Stabila provideTron(@ApplicationContext Context context, StabilaNetwork tronNetwork, TokenManager tokenManager,
                               CustomPreference customPreference, AccountManager accountManager, WalletAppManager walletAppManager) {
        return new Stabila(context, tronNetwork, customPreference, accountManager, walletAppManager, tokenManager);
    }

    @Provides
    @Singleton
    static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .addMigrations(AppDatabase.MIGRATION_2_3)
                .addMigrations(AppDatabase.MIGRATION_3_4)
                .addMigrations(AppDatabase.MIGRATION_4_5)
                .addMigrations(AppDatabase.MIGRATION_5_6)
                .build();
    }

    @Provides
    @Singleton
    static RxJavaSchedulers provideRxJavaSchedulers() {
        return new RxJavaSchedulersImpl();
    }
}
