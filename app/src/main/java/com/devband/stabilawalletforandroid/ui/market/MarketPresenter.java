package com.devband.stabilawalletforandroid.ui.market;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

/**
 * Created by user on 2018. 5. 24..
 */

public class MarketPresenter extends BasePresenter<MarketView> {

    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public MarketPresenter(MarketView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {
        loadMarket();
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

    private void loadMarket() {
        mView.showLoadingDialog();

        this.mStabilaNetwork
        .getMarkets()
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(
                markets -> mView.marketDataLoadSuccess(markets),
                t -> mView.showServerError()
        );
    }
}
