package com.devband.stabilawalletforandroid.ui.importkey;

import com.crashlytics.android.Crashlytics;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ImportPrivateKeyPresenter extends BasePresenter<ImportPrivateKeyView> {

    private Stabila mTron;
    private RxJavaSchedulers mRxJavaSchedulers;

    public ImportPrivateKeyPresenter(ImportPrivateKeyView view, Stabila tron, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mRxJavaSchedulers = rxJavaSchedulers;
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

    public void createWallet(String privateKey, String password) {
        mTron.createWallet(password)
                .flatMap(result -> {
                    if (result == WalletAppManager.SUCCESS) {
                        return mTron.importAccount(Constants.PREFIX_ACCOUNT_NAME, privateKey, password);
                    }

                    return Single.just(result);
                })
                .map(result -> {
                    if (result != Stabila.SUCCESS) {
                        return result;
                    }

                    result = mTron.login(password);

                    if (result != Stabila.SUCCESS) {
                        return result;
                    }

                    mTron.agreeTerms(true);
                    return Stabila.SUCCESS;
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
                            mView.createdWallet();
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
