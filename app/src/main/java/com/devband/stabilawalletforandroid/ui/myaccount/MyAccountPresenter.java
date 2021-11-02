package com.devband.stabilawalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.main.dto.Asset;
import com.devband.stabilawalletforandroid.ui.main.dto.Cded;
import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.stabilawalletforandroid.database.model.AccountModel;
import com.devband.stabilawalletforandroid.database.model.FavoriteTokenModel;
import com.devband.stabilawalletforandroid.database.model.Trc10AssetModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;
import com.devband.stabilawalletforandroid.stabila.exception.InvalidPasswordException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.stabila.protos.Protocol;

public class MyAccountPresenter extends BasePresenter<MyAccountView> {

    private Stabila mStabila;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;
    private FavoriteTokenDao mFavoriteTokenDao;

    public MyAccountPresenter(MyAccountView view, Stabila stabila, WalletAppManager walletAppManager,
                              RxJavaSchedulers rxJavaSchedulers, AppDatabase appDatabase) {
        super(view);
        this.mStabila = stabila;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mFavoriteTokenDao = appDatabase.favoriteTokenDao();
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

    public Single<List<AccountModel>> getAccountList() {
        return mStabila.getAccountList();
    }

    public void getAccountAccountInfo() {
        final String loginAddress = mStabila.getLoginAddress();

        mView.showLoadingDialog();

        mStabila.queryAccount(loginAddress)
                .map((account -> {
                    List<Cded> cdedList = new ArrayList<>();

                    for (Protocol.Account.Cded cded : account.getCdedList()) {
                        cdedList.add(Cded.builder()
                                .cdedBalance(cded.getCdedBalance())
                                .expireTime(cded.getExpireTime())
                                .build());
                    }

                    long accountId = mStabila.getLoginAccount().getId();
                    List<Asset> assetList = new ArrayList<>();

//                        for (Trc20Token trc20TokenBalance : account.getTrc20TokenBalances()) {
//                            assetList.add(Asset.builder()
//                                    .name(trc20TokenBalance.getName())
//                                    .displayName("[TRC20] " + trc20TokenBalance.getName())
//                                    .balance(trc20TokenBalance.getBalance() / Math.pow(10, trc20TokenBalance.getDecimals()))
//                                    .build());
//                        }

                    for (String key : account.getAssetV2Map().keySet()) {
                        Trc10AssetModel trc10Asset = mStabila.getTrc10Asset(key);

                        assetList.add(Asset.builder()
                                .name(key)
                                .displayName("[" + key + "]" + trc10Asset.getName())
                                .balance(trc10Asset.getPrecision() > 0 ?
                                        account.getAssetV2Map().get(key) / Math.pow(10, trc10Asset.getPrecision())
                                        : account.getAssetV2Map().get(key))
                                .build());
                    }

                    return StabilaAccount.builder()
                            .balance(account.getBalance())
                            //.bandwidth(account.getDelegatedCdedBalanceForBandwidth())
                            .assetList(assetList)
                            .cdedList(cdedList)
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
                mView.displayAccountInfo(loginAddress, account);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // todo - error msg
                if (e instanceof ConnectException) {
                    // internet error
                }

                mView.showServerError();
                mView.displayAccountInfo(loginAddress, null);
            }
        });
    }



    public String getLoginPrivateKey(@NonNull String password) {
        return mStabila.getLoginPrivateKey(password);
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel) {
        mStabila.changeLoginAccount(accountModel);
    }

    public void cdBalance(@NonNull String password, long cdBalance) {
        mView.showLoadingDialog();

        mStabila.cdBalance(password, cdBalance, Constants.CD_DURATION)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successCdBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof InvalidPasswordException) {
                    mView.showInvalidPasswordMsg();
                } else {
                    e.printStackTrace();
                    mView.showServerError();
                }
            }
        });
    }

    public void uncdBalance(@NonNull String password) {
        mView.showLoadingDialog();

        mStabila.uncdBalance(password)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successCdBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RuntimeException) {
                    mView.unableToUncd();
                } else {
                    mView.showServerError();
                }
            }
        });
    }

    public long getLoginAccountIndex() {
        return mStabila.getLoginAccount().getId();
    }

    public AccountModel getLoginAccount() {
        return mStabila.getLoginAccount();
    }

    public int getAccountCount() {
        return mStabila.getAccountCount();
    }

    public boolean isFavoriteToken(@NonNull String tokenName) {
        if (mStabila.getLoginAccount() != null) {
            long accountId = mStabila.getLoginAccount().getId();

            return mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, tokenName) != null;
        }

        return false;
    }

    public void doFavorite(@NonNull String tokenName) {
        if (mStabila.getLoginAccount() != null) {
            long accountId = mStabila.getLoginAccount().getId();
            FavoriteTokenModel model = FavoriteTokenModel.builder()
                    .accountId(accountId)
                    .tokenName(tokenName)
                    .build();

            mFavoriteTokenDao.insert(model);
        }
    }

    public void removeFavorite(@NonNull String tokenName) {
        if (mStabila.getLoginAccount() != null) {
            long accountId = mStabila.getLoginAccount().getId();
            FavoriteTokenModel favoriteTokenModel = mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, tokenName);
            mFavoriteTokenDao.delete(favoriteTokenModel);
        }
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void removeAccount(long accountId, String accountName) {
        mView.showLoadingDialog();
        Single.fromCallable(() -> {
            mStabila.removeAccount(accountId, accountName);
            return true;
        })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe((result) -> mView.successDelete());
    }
}
