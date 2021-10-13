package com.devband.stabilawalletforandroid.ui.backupaccount;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class BackupAccountPresenter extends BasePresenter<BackupAccountView> {

    private Stabila mStabila;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public BackupAccountPresenter(BackupAccountView view, Stabila stabila, WalletAppManager walletAppManager,
                                  RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
        this.mWalletAppManager = walletAppManager;
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

    public void agreeTerms(boolean isAgree) {
        Single.fromCallable(() -> {
            mWalletAppManager.agreeTerms(true);
            return true;
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                mView.startMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                mView.startMainActivity();
            }
        });
    }

    public void getAccountAndPrivateKey(byte[] aesKey) {
        String address = mStabila.getLoginAddress();
        String privateKey = mStabila.getLoginPrivateKey(aesKey);

        mView.displayAccountInfo(address, privateKey);
    }
}
