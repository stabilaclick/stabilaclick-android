package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.main.dto.Asset;
import com.devband.stabilawalletforandroid.ui.main.dto.Cded;
import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.stabilascan.Balance;
import com.devband.stabilalib.stabilascan.CdedStb;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class OverviewPresenter extends BasePresenter<OverviewView> {

    private StabilaNetwork mStabilaNetwork;
    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;

    public OverviewPresenter(OverviewView view, Stabila stabila, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
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

    public void getAccount(@NonNull String address) {
        mView.showLoadingDialog();

        Single.zip(mStabila.getAccount(address), mStabilaNetwork.getTransactionStats(address), ((account, transactionStats) -> {
            List<Cded> cdedList = new ArrayList<>();

            for (CdedStb cded : account.getCded().getBalances()) {
                cdedList.add(Cded.builder()
                        .cdedBalance(cded.getAmount())
                        .expireTime(cded.getExpires())
                        .build());
            }

            List<Asset> assetList = new ArrayList<>();

            for (Balance balance : account.getTrc10TokenBalances()) {
                assetList.add(Asset.builder()
                        .name(balance.getDisplayName() + "(" + balance.getName() + "):")
                        .balance(balance.getBalance())
                        .build());
            }

            return StabilaAccount.builder()
                    .balance(account.getBalance())
                    .bandwidth(account.getBandwidth().getNetRemaining())
                    .assetList(assetList)
                    .cdedList(cdedList)
                    .transactions(transactionStats.getTransactions())
                    .transactionIn(transactionStats.getTransactionsIn())
                    .transactionOut(transactionStats.getTransactionsOut())
                    .account(account)
                    .build();
        }))
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<StabilaAccount>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(StabilaAccount account) {
                mView.finishLoading(account);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // todo - error msg
                if (e instanceof ConnectException) {
                    // internet error
                }

                mView.showServerError();
            }
        });
    }
}
