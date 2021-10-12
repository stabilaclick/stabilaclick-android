package com.devband.stabilawalletforandroid.ui.accountdetail.representative;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Transfer;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.model.BaseModel;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.model.BlockStatModel;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.model.HeaderModel;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.model.TransferHistoryModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class RepresentativePresenter extends BasePresenter<RepresentativeView> {

    private StabilaNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public RepresentativePresenter(RepresentativeView view, StabilaNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
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

    public void loadData(String address) {
        mView.showLoadingDialog();

        Single.zip(
                loadBlockStat(address),
                loadTransferHistory(address),
                (blockStat, transfers) -> {
                    List<BaseModel> viewModels = new ArrayList<>();

                    //add block stat
                    viewModels.add(blockStat);

                    //add transfer list
                    if (transfers != null && transfers.size() > 0) {
                        viewModels.add(new HeaderModel(mContext.getString(R.string.list_header_transfers)));
                        for (Transfer transfer : transfers) {
                            viewModels.add(new TransferHistoryModel(transfer));
                        }
                    }
                    return viewModels;
                })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        viewModels -> {
                            if (mView != null) {
                                mView.dataLoadSuccess(viewModels);
                            }
                        }, t -> {
                            if (mView != null) {
                                mView.showServerError();
                            }
                        }
                );
    }

    private Single<BlockStatModel> loadBlockStat(String address) {
        return Single.zip(
                mTronNetwork.getAccountMedia(address),
                mTronNetwork.getTransactionStats(address),
                (accountMedia, transactionStats) ->
                        new BlockStatModel(
                                address,
                                accountMedia.getImageUrl(),
                                transactionStats.getTransactionsIn(),
                                transactionStats.getTransactionsOut()
                        )
        );
    }

    private Single<List<Transfer>> loadTransferHistory(String address) {
        return mTronNetwork.getTransfers(0, 25, "-timestamp", true, address)
                .map(value -> value.getData());
    }
}
