package com.devband.stabilawalletforandroid.ui.token.overview;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Token;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class OverviewPresenter extends BasePresenter<OverviewView> {


    private StabilaNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public OverviewPresenter(OverviewView view, StabilaNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTronNetwork = tronNetwork;
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

    public void loadTokenInfo(@NonNull String tokenName) {
        mView.showLoadingDialog();

        mTronNetwork
                .getTokenDetail(tokenName)
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Token token) {
                        mView.tokenInfoLoadSuccess(token);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
