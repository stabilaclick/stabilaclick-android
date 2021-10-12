package com.devband.stabilawalletforandroid.ui.login;

import android.support.annotation.Nullable;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginView> {

    private Stabila mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;
    private final CustomPreference mCustomPreference;

    public LoginPresenter(LoginView view, Stabila tron, WalletAppManager walletAppManager,
                          RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mCustomPreference = customPreference;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void loginWallet(@Nullable String password) {
        Single.fromCallable(() -> {
            if (!mCustomPreference.getMigrationDb()) {
                boolean loginResult = mWalletAppManager.oldLogin(password);

                if (!loginResult) {
                    return WalletAppManager.ERROR;
                } else {
                    mTron.migrationOldData(password);
                    mCustomPreference.setMigrationDb(true);

                    int res = mTron.login(password);

                    if (res != Stabila.SUCCESS) {
                        return WalletAppManager.ERROR;
                    }

                    return Stabila.SUCCESS;
                }
            } else {
                int result = mWalletAppManager.login(password);

                if (result == WalletAppManager.SUCCESS) {
                    int res = mTron.login(password);
                    if (res != Stabila.SUCCESS) {
                        return WalletAppManager.ERROR;
                    }
                }

                return result;
            }
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer result) {
                mView.loginResult(result);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.loginResult(Stabila.ERROR_LOGIN);
            }
        });
    }
}
