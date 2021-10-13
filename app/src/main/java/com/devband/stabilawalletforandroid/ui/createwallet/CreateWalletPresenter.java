package com.devband.stabilawalletforandroid.ui.createwallet;

import android.os.Build;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;
    private CustomPreference mCustomPreference;

    public CreateWalletPresenter(CreateWalletView view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers,
                                 CustomPreference customPreference) {
        super(view);
        this.mStabila = stabila;
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

    public void createWallet(@NonNull String password) {
        mStabila.createWallet(password)
                .flatMap(createWalletResult -> {
                    if (createWalletResult == WalletAppManager.SUCCESS) {
                        return mStabila.createAccount(Constants.PREFIX_ACCOUNT_NAME, password);
                    } else {
                        return Single.just(Stabila.ERROR);
                    }
                })
                .map(registerAccountResult -> {
                    if (registerAccountResult != Stabila.SUCCESS) {
                        return registerAccountResult;
                    } else {
                        int result = mStabila.login(password);

                        if (result == WalletAppManager.SUCCESS) {
                            mCustomPreference.setInitWallet(true);
                            mCustomPreference.setKeyStoreVersion(Build.VERSION.SDK_INT);
                            return Stabila.SUCCESS;
                        }

                        return Stabila.ERROR_INVALID_PASSWORD;
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
                        if (result == Stabila.ERROR_INVALID_PASSWORD) {
                            mView.passwordError();
                        } else if (result == Stabila.ERROR) {
                            mView.passwordError();
                        } else {
                            mView.createdWallet(WalletAppManager.getEncKey(password));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                        mView.registerWalletError();
                    }
                });
    }
}
