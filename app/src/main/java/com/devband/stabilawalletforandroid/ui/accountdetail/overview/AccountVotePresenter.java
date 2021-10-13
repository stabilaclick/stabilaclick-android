package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.AccountVote;
import com.devband.stabilalib.dto.AccountVotes;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AccountVotePresenter extends BasePresenter<AccountVoteView> {

    private AdapterDataModel<AccountVote> mAdapterDataModel;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public AccountVotePresenter(AccountVoteView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<AccountVote> adapterDataModel) {
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

    public void getVotes(@NonNull String address, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        mStabilaNetwork.getAccountVotes(address, startIndex, pageSize, "-votes")
                .observeOn(mRxJavaSchedulers.getMainThread())
                .map(accountVotes -> {
                    for (AccountVote accountVote : accountVotes.getData()) {
                        accountVote.setTotalVotes(accountVotes.getTotalVotes());
                    }

                    return accountVotes;
                })
                .subscribe(new SingleObserver<AccountVotes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountVotes accountVotes) {
                        mAdapterDataModel.addAll(accountVotes.getData());
                        mView.finishLoading(accountVotes.getTotalVotes(), accountVotes.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
