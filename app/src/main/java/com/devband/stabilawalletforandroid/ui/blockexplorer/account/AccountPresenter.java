package com.devband.stabilawalletforandroid.ui.blockexplorer.account;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.StabilaAccount;
import com.devband.stabilalib.dto.StabilaAccounts;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AccountPresenter extends BasePresenter<AccountView> {

    private AdapterDataModel<StabilaAccount> mAdapterDataModel;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public AccountPresenter(AccountView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<StabilaAccount> adapterDataModel) {
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

    public void getStabilaAccounts(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        Single.zip(mStabilaNetwork.getAccounts(startIndex, pageSize, "-balance"), mStabilaNetwork.getCoinInfo(Constants.STABILA_COINMARKET_NAME),
                ((stabilaAccounts, coinMarketCaps) -> {
                    for (StabilaAccount stabilaAccount : stabilaAccounts.getData()) {
                        stabilaAccount.setTotalSupply((long) Double.parseDouble(coinMarketCaps.get(0).getTotalSupply()));
                        stabilaAccount.setAvailableSypply((long) Double.parseDouble(coinMarketCaps.get(0).getAvailableSupply()));
                        stabilaAccount.setBalancePercent(((double) stabilaAccount.getBalance() / Constants.ONE_STB / (double) stabilaAccount.getAvailableSypply()) * 100f);
                    }

                    return stabilaAccounts;
                }))
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<StabilaAccounts>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(StabilaAccounts stabilaAccounts) {
                        mAdapterDataModel.addAll(stabilaAccounts.getData());
                        mView.finishLoading(stabilaAccounts.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
