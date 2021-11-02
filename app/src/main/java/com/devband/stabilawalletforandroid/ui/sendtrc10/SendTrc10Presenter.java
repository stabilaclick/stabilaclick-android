package com.devband.stabilawalletforandroid.ui.sendtrc10;

import com.devband.stabilawalletforandroid.ui.main.dto.Asset;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.database.model.Trc10AssetModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.exception.InvalidAddressException;
import com.devband.stabilawalletforandroid.stabila.exception.InvalidPasswordException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SendTrc10Presenter extends BasePresenter<SendTrc10View> {

    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;

    public SendTrc10Presenter(SendTrc10View view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
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
        mStabila.queryAccount(mStabila.getLoginAddress())
            .subscribeOn(mRxJavaSchedulers.getIo())
            .map(account -> {
                List<Asset> assets = new ArrayList<>();

                assets.add(Asset.builder()
                        .name(Constants.STABILA_SYMBOL)
                        .displayName(Constants.STABILA_SYMBOL)
                        .balance(((double) account.getBalance()) / Constants.ONE_STB)
                        .build());

                for (String key : account.getAssetV2Map().keySet()) {
                    Trc10AssetModel trc10Asset = mStabila.getTrc10Asset(key);

                    assets.add(Asset.builder()
                            .name(trc10Asset.getName())
                            .displayName("[" + key + "]" + trc10Asset.getName())
                            .balance(trc10Asset.getPrecision() > 0 ?
                                    account.getAssetV2Map().get(key) / Math.pow(10, trc10Asset.getPrecision())
                                    : account.getAssetV2Map().get(key))
                            .precision(trc10Asset.getPrecision())
                            .build());
                }

                return assets;
            })
            .observeOn(mRxJavaSchedulers.getMainThread())
            .subscribe(assets -> {
                mView.displayAccountInfo(assets);
            }, e -> e.printStackTrace());
    }

    @Override
    public void onDestroy() {

    }

    public void sendStabila(String password, String toAddress, long amount) {
        if (!mStabila.isLogin()) {
            mView.invalidPassword();
            return;
        }

        mStabila.sendCoin(password, toAddress, amount)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                mView.sendTokenResult(result);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof InvalidAddressException) {
                    mView.invalidAddress();
                } else if (e instanceof InvalidPasswordException) {
                    mView.invalidPassword();
                } else if (e instanceof RuntimeException) {
                    System.out.println("=======================================================3");
                    e.printStackTrace();
                    mView.connectionError();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void transferAsset(String password, String toAddress, String assetName, long amount) {
        if (!mStabila.isLogin()) {
            mView.invalidPassword();
            return;
        }

        mStabila.transferAsset(password, toAddress, assetName, amount)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                mView.sendTokenResult(result);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof InvalidAddressException) {
                    mView.invalidAddress();
                } else if (e instanceof InvalidPasswordException) {
                    mView.invalidPassword();
                } else if (e instanceof RuntimeException) {
                    System.out.println("=======================================================1");
                    e.printStackTrace();
                    mView.connectionError();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
