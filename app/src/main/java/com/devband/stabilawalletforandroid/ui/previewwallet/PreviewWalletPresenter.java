package com.devband.stabilawalletforandroid.ui.previewwallet;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.database.model.AccountModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

public class PreviewWalletPresenter extends BasePresenter<PreviewWalletView> {

    private Stabila mStabila;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private AdapterDataModel<AccountModel> mAdapterDataModel;

    public PreviewWalletPresenter(PreviewWalletView view, Stabila stabila, StabilaNetwork stabilaNetwork,
                                  RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<AccountModel> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        refreshAccount();
    }

    public void refreshAccount() {
        mStabila.getAccountList()
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(accountList -> {
                    mAdapterDataModel.clear();
                    mAdapterDataModel.addAll(accountList);
                    mView.finishRefresh();
                }, e -> mView.finishRefresh());
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
}
