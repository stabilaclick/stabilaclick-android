package com.devband.stabilawalletforandroid.ui.accountdetail.transaction;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Transaction;
import com.devband.stabilalib.dto.Transactions;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TransactionPresenter extends BasePresenter<TransactionView> {

    private AdapterDataModel<Transaction> mAdapterDataModel;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransactionPresenter(TransactionView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<Transaction> adapterDataModel) {
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

    public void getTransactions(long block, long startIndex, int pageSize) {
        mView.showLoadingDialog();
        mStabilaNetwork.getTransactions(block, startIndex, pageSize, "-timestamp", true)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Transactions>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Transactions transactions) {
                mAdapterDataModel.addAll(transactions.getData());
                mView.finishLoading(transactions.getTotal());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public void getTransactions(String address, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        mStabilaNetwork.getTransactions(address, startIndex, pageSize, "-timestamp", true)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Transactions>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Transactions transactions) {
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
