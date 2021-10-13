package com.devband.stabilawalletforandroid.ui.blockexplorer.overview;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

/**
 * Created by user on 2018. 5. 28..
 */

public class OverviewPresenter extends BasePresenter<OverviewView> {

    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public OverviewPresenter(OverviewView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
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

    void chartDataLoad() {
        mView.showLoadingDialog();

        mStabilaNetwork.getAccounts(0, 10, "-balance")
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::overviewDataLoadSuccess,
                        t -> mView.showServerError()
                );
    }
}
