package com.devband.stabilawalletforandroid.ui.mytransfer;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Transfer;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.database.model.Trc10AssetModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.TokenManager;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransferPresenter extends BasePresenter<TransferView> {

    private AdapterDataModel<TransferInfo> mAdapterDataModel;
    private Stabila mTron;
    private StabilaNetwork mTronNetwork;
    private TokenManager mTokenManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransferPresenter(TransferView view, Stabila tron, StabilaNetwork tronNetwork, TokenManager tokenManager,
                             RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mTokenManager = tokenManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<TransferInfo> adapterDataModel) {
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

    public void loadTransfer(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        String address = mTron.getLoginAddress();

        mTronNetwork.getTransfersByAddress("-timestamp", true, pageSize, startIndex, address)
        .map(transactions -> {
            List<TransferInfo> infos = new ArrayList<>();

            for (Transfer t : transactions.getData()) {
                TransferInfo info = new TransferInfo();
                info.setHash(t.getHash());
                info.setAmount(t.getAmount());
                info.setBlock(t.getBlock());
                info.setTimestamp(t.getTimestamp());
                if ("_".equalsIgnoreCase(t.getTokenName())) {
                    info.setTokenName(Constants.TRON_SYMBOL);
                    info.setPrecision(Constants.TRX_PRECISION);
                } else {
                    Trc10AssetModel trc10AssetModel = mTokenManager.getTokenInfo(t.getTokenName()).blockingGet();
                    info.setTokenName(trc10AssetModel.getName());
                    info.setPrecision(trc10AssetModel.getPrecision());
                }
                info.setSend(t.getTransferFromAddress().equalsIgnoreCase(address));
                info.setTransferFromAddress(t.getTransferFromAddress());
                info.setTransferToAddress(t.getTransferToAddress());
                info.setConfirmed(t.isConfirmed());
                info.setTotal(transactions.getTotal());
                infos.add(info);
            }

            return infos;
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers .getMainThread())
        .subscribe(new SingleObserver<List<TransferInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<TransferInfo> transactionInfos) {
                mAdapterDataModel.addAll(transactionInfos);

                long total = 0;

                if (!transactionInfos.isEmpty()) {
                    total = transactionInfos.get(0).getTotal();
                }

                mView.transferDataLoadSuccess(total);
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public void clearData() {
        mAdapterDataModel.clear();
    }
}
