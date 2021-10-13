package com.devband.stabilawalletforandroid.ui.address;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import net.glxn.qrgen.android.QRCode;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import lombok.Builder;

public class AddressPresenter extends BasePresenter<AddressView> {

    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;

    public AddressPresenter(AddressView view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers) {
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
        Single.fromCallable(() -> {
            String address = mStabila.getLoginAddress();

            if (address == null || address.isEmpty()) {
                return null;
            }

            Bitmap addressImage = QRCode.from(address).bitmap();

            return AddressInfo.builder()
                    .addressBitmap(addressImage)
                    .address(address)
                    .build();
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<AddressInfo>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(@Nullable AddressInfo addressInfo) {
                mView.addressResult(addressInfo);
            }

            @Override
            public void onError(Throwable e) {
                mView.addressResult(null);
            }
        });
    }

    @Override
    public void onDestroy() {

    }

    @Builder
    public static class AddressInfo {

        public Bitmap addressBitmap;
        public String address;
    }
}
