package com.devband.stabilawalletforandroid.ui.token.holder;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.TokenHolder;
import com.devband.stabilalib.dto.TokenHolders;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class HolderPresenter extends BasePresenter<HolderView> {

    private AdapterDataModel<TokenHolder>  mAdapterDataModel;
    private StabilaNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public HolderPresenter(HolderView view, StabilaNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<TokenHolder> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
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

    public void getTokenHolders(String tokenName, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        Single.zip(mTronNetwork.getTokenHolders(tokenName, startIndex, pageSize, "-balance"), mTronNetwork.getTokenDetail(tokenName),
                ((tokenHolders, token) -> {
                    for (TokenHolder tokenHolder : tokenHolders.getData()) {
                        tokenHolder.setTotalSupply(token.getTotalSupply());
                        tokenHolder.setBalancePercent(((double) tokenHolder.getBalance() / (double) token.getTotalSupply()) * 100f);
                    }

                    return tokenHolders;
                }))
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<TokenHolders>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TokenHolders tokenHolders) {
                        mAdapterDataModel.addAll(tokenHolders.getData());
                        mView.finishLoading(tokenHolders.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
