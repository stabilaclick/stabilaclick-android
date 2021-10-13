package com.devband.stabilawalletforandroid.ui.token.transfer;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Transfer;
import com.devband.stabilalib.dto.Transfers;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TransferPresenter extends BasePresenter<TransferView> {

    private AdapterDataModel<Transfer> mAdapterDataModel;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransferPresenter(TransferView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<Transfer> adapterDataModel) {
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

    public void getTransfer(long startIndex, int pageSize, String tokenName) {
        mView.showLoadingDialog();

        mStabilaNetwork
                .getTransfers("-timestamp", true, pageSize, startIndex, tokenName)
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Transfers>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Transfers transactions) {
                        mAdapterDataModel.addAll(transactions.getData());
                        mView.finishLoading(transactions.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
