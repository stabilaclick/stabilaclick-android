package com.devband.stabilawalletforandroid.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.main.dto.Asset;
import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.CoinMarketCap;
import com.devband.stabilalib.stabilascan.Trc20Token;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.common.CustomPreference;
import com.devband.stabilawalletforandroid.common.Hex2Decimal;
import com.devband.stabilawalletforandroid.database.AppDatabase;
import com.devband.stabilawalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.stabilawalletforandroid.database.dao.Trc20ContractDao;
import com.devband.stabilawalletforandroid.database.model.AccountModel;
import com.devband.stabilawalletforandroid.database.model.Trc10AssetModel;
import com.devband.stabilawalletforandroid.database.model.Trc20ContractModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;
import com.devband.stabilawalletforandroid.ui.main.dto.Cded;

import org.spongycastle.util.encoders.Hex;
import org.stabila.api.GrpcAPI;
import org.stabila.protos.Protocol;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {

    private AdapterDataModel<Asset> mAdapterDataModel;
    private Stabila mStabila;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private CustomPreference mCustomPreference;
    private FavoriteTokenDao mFavoriteTokenDao;
    private Trc20ContractDao mTrc20ContractDao;
    private WalletAppManager mWalletAppManager;

    public MainPresenter(MainView view, Stabila stabila, WalletAppManager walletAppManager, StabilaNetwork stabilaNetwork,
                         RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference, AppDatabase appDatabase) {
        super(view);
        this.mStabila = stabila;
        this.mWalletAppManager = walletAppManager;
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mCustomPreference = customPreference;
        this.mFavoriteTokenDao = appDatabase.favoriteTokenDao();
        this.mTrc20ContractDao = appDatabase.trc20ContractDao();
    }

    public void setAdapterDataModel(AdapterDataModel<Asset> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        syncTrc20TokenContracts();
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

    public boolean isLogin() {
        return mStabila.isLogin();
    }

    public void getMyAccountAllAssetsInfo() {
        mView.showLoadingDialog();
        String loginAddress = mStabila.getLoginAddress();

        if (!TextUtils.isEmpty(loginAddress)) {
            List<Cded> cdedList = new ArrayList<>();
            List<Asset> assetList = new ArrayList<>();

                assetList.add(Asset.builder()
                        .name("STB")
                        .displayName("STB")
                        .balance(mStabila.queryAccount(loginAddress).blockingGet().getBalance() / Constants.ONE_STB)
                        .build());
            mStabila.queryAccount(loginAddress)
                    .map((account -> {
                        GrpcAPI.AccountResourceMessage accountResourceMessage = mStabila.queryAccountResourceMessage(loginAddress).blockingGet();
                        for (Protocol.Account.Cded cded : account.getCdedList()) {
                            cdedList.add(Cded.builder()
                                    .cdedBalance(cded.getCdedBalance())
                                    .expireTime(cded.getExpireTime())
                                    .build());
                        }

                        long accountId = mStabila.getLoginAccount().getId();

                        for (String key : account.getAssetV2Map().keySet()) {
                            boolean isFavorite = mCustomPreference.isFavoriteToken(accountId);

                            if (!isFavorite || (isFavorite && mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, key) != null)) {
                                Trc10AssetModel trc10Asset = mStabila.getTrc10Asset(key);

                                assetList.add(Asset.builder()
                                        .name(key)
                                        .displayName("[" + key + "]" + trc10Asset.getName())
                                        .balance(trc10Asset.getPrecision() > 0 ?
                                                account.getAssetV2Map().get(key) / Math.pow(10, trc10Asset.getPrecision())
                                                : account.getAssetV2Map().get(key))
                                        .build());

                            }
                        }

                        // TRC20
                        List<Trc20ContractModel> list = mTrc20ContractDao.getAll();

                        for (Trc20ContractModel trc20ContractModel : list) {
                            GrpcAPI.TransactionExtention transactionExtention = mStabila.getTrc20Balance(loginAddress, trc20ContractModel.getAddress()).blockingGet();

                            if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
                                Timber.d("RPC create call trx failed!");
                                Timber.d("Code = " + transactionExtention != null ? String.valueOf(transactionExtention.getResult().getCode()) : "null");
                                Timber.d("Message = " + transactionExtention != null ? transactionExtention.getResult().getMessage().toStringUtf8() : "null");
                                continue;
                            }

                            Protocol.Transaction resultTransaction = transactionExtention.getTransaction();

                            if (resultTransaction.getRetCount() != 0 &&
                                    transactionExtention.getConstantResult(0) != null &&
                                    transactionExtention.getResult() != null) {
                                byte[] result = transactionExtention.getConstantResult(0).toByteArray();

                                long balance = Hex2Decimal.hex2Decimal(Hex.toHexString(result));

                                assetList.add(Asset.builder()
                                        .name(trc20ContractModel.getName())
                                        .displayName(trc20ContractModel.getName() + "(" + trc20ContractModel.getSymbol() + ")")
                                        .balance(balance / Math.pow(10, trc20ContractModel.getPrecision()))
                                        .build());
                            }
                        }
                        return StabilaAccount.builder()
                                .balance(account.getBalance())
                                .bandwidthUsed(accountResourceMessage.getNetUsed())
                                .bandwidthLimit(accountResourceMessage.getNetLimit())
                                .ucrUsed(accountResourceMessage.getUcrUsed())
                                .ucrLimit(accountResourceMessage.getUcrLimit())
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
                            mView.displayAccountInfo(account);
                            mAdapterDataModel.clear();
                            mAdapterDataModel.addAll(account.getAssetList());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            // todo - error msg
                            if (e instanceof ConnectException) {
                                // internet error
                            } else if (e instanceof IllegalArgumentException) {
                                mView.goToIntroActivity();
                            }

                            mView.connectionError();
                        }
                    });
        } else {
            mView.goToIntroActivity();
        }
    }

    public void getMyAccountTrc10Info() {
        mView.showLoadingDialog();
        String loginAddress = mStabila.getLoginAddress();

        if (!TextUtils.isEmpty(loginAddress)) {
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

                        // TRC10 list
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
                            mView.displayAccountInfo(account);
                            mAdapterDataModel.clear();
                            mAdapterDataModel.addAll(account.getAssetList());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            // todo - error msg
                            if (e instanceof ConnectException) {
                                // internet error
                            } else if (e instanceof IllegalArgumentException) {
                                mView.goToIntroActivity();
                            }

                            mView.connectionError();
                        }
                    });
        } else {
            mView.goToIntroActivity();
        }
    }


    public void getMyAccountTrc20Info(boolean isHideNoBalance) {
        mAdapterDataModel.clear();

        mView.showLoadingDialog();
        String loginAddress = mStabila.getLoginAddress();

        if (!TextUtils.isEmpty(loginAddress)) {
            Single<List<Asset>> trc20List = Single.fromCallable(() -> {
                List<Trc20ContractModel> list = mTrc20ContractDao.getAll();

                List<Asset> assetList = new ArrayList<>();

                for (Trc20ContractModel trc20ContractModel : list) {
                    GrpcAPI.TransactionExtention transactionExtention = mStabila.getTrc20Balance(loginAddress, trc20ContractModel.getAddress()).blockingGet();

                    if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
                        Timber.d("RPC create call trx failed!");
                        Timber.d("Code = " + transactionExtention != null ? String.valueOf(transactionExtention.getResult().getCode()) : "null");
                        Timber.d("Message = " + transactionExtention != null ? transactionExtention.getResult().getMessage().toStringUtf8() : "null");
                        continue;
                    }

                    Protocol.Transaction resultTransaction = transactionExtention.getTransaction();

                    if (resultTransaction.getRetCount() != 0 &&
                            transactionExtention.getConstantResult(0) != null &&
                            transactionExtention.getResult() != null) {
                        byte[] result = transactionExtention.getConstantResult(0).toByteArray();
//                        System.out.println("message:" + resultTransaction.getRet(0).getRet());
//                        System.out.println(":" + ByteArray
//                                .toStr(transactionExtention.getResult().getMessage().toByteArray()));
//                        System.out.println("Result:" + Hex2Decimal.hex2Decimal(Hex.toHexString(result)));

                        long balance = Hex2Decimal.hex2Decimal(Hex.toHexString(result));

                        if (isHideNoBalance && balance == 0) {
                            continue;
                        }

                        assetList.add(Asset.builder()
                                .name(trc20ContractModel.getName())
                                .displayName(trc20ContractModel.getName() + "(" + trc20ContractModel.getSymbol() + ")")
                                .balance(balance / Math.pow(10, trc20ContractModel.getPrecision()))
                                .build());
                    }
                }

                return assetList;
            });

            Single.zip(mStabila.queryAccount(loginAddress), trc20List, (account, trc20) -> {
                List<Cded> cdedList = new ArrayList<>();

                for (Protocol.Account.Cded cded : account.getCdedList()) {
                    cdedList.add(Cded.builder()
                            .cdedBalance(cded.getCdedBalance())
                            .expireTime(cded.getExpireTime())
                            .build());
                }

                return StabilaAccount.builder()
                        .balance(account.getBalance())
                        //.bandwidth(account.getDelegatedCdedBalanceForBandwidth())
                        .assetList(trc20)
                        .cdedList(cdedList)
                        .build();
            })
                    .subscribeOn(mRxJavaSchedulers.getIo())
                    .observeOn(mRxJavaSchedulers.getMainThread())
                    .subscribe(new SingleObserver<StabilaAccount>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(StabilaAccount account) {
                            mView.displayAccountInfo(account);
                            mAdapterDataModel.clear();
                            mAdapterDataModel.addAll(account.getAssetList());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            // todo - error msg
                            if (e instanceof ConnectException) {
                                // internet error
                            } else if (e instanceof IllegalArgumentException) {
                                mView.goToIntroActivity();
                            }

                            mView.connectionError();
                        }
                    });
        } else {
            mView.goToIntroActivity();
        }
    }

    public void getStabilaMarketInfo() {
        mStabilaNetwork.getCoinInfo(Constants.STABILA_COINMARKET_NAME)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps.size() > 0) {
                            mView.setStabilaMarketInfo(coinMarketCaps.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public boolean logout() {
        mStabila.logout();

        return true;
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mStabila.getLoginAccount();
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        return mStabila.changeLoginAccountName(accountName);
    }

    public void createAccount(@NonNull String nickname, @NonNull String password) {
        mStabila.createAccount(nickname, password)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result == Stabila.SUCCESS) {
                            mView.successCreateAccount();
                        } else {
                            mView.showInvalidPasswordMsg();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public Single<List<AccountModel>> getAccountList() {
        return mStabila.getAccountList();
    }

    public boolean changeLoginAccount(@NonNull AccountModel accountModel) {
        return mStabila.changeLoginAccount(accountModel);
    }

    public void importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull String password) {
        mStabila.importAccount(nickname, privateKey, password)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result == Stabila.SUCCESS) {
                            mView.successImportAccount();
                        } else if (result == Stabila.ERROR_EXIST_ACCOUNT) {
                            mView.duplicatedAccount();
                        } else if (result == Stabila.ERROR_PRIVATE_KEY) {
                            mView.failCreateAccount();
                        } else if (result == Stabila.ERROR_INVALID_PASSWORD) {
                            mView.showInvalidPasswordMsg();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.failCreateAccount();
                    }
                });
    }

    public long getLoginAccountIndex() {
        return mStabila.getLoginAccount().getId();
    }

    public void setOnlyFavorites(boolean isFavorites) {
        if (mStabila.getLoginAccount() != null) {
            mCustomPreference.setFavoriteToken(mStabila.getLoginAccount().getId(), isFavorites);
        }
    }

    public boolean getIsFavoritesTokens() {
        if (mStabila.getLoginAccount() != null) {
            return mCustomPreference.isFavoriteToken(mStabila.getLoginAccount().getId());
        }

        return false;
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        mView.showChangePasswordDialog();

        Single.fromCallable(() -> mStabila.changePassword(oldPassword, newPassword))
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        mView.changePasswordResult(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.changePasswordResult(false);
                    }
                });
    }

    public void addTrc20Contract(String name, String symbol, String contractAddress, int precision) {
        mView.showLoadingDialog();

        mStabila.checkTrc20Contract(contractAddress)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .map(result -> {
                    if (result == Stabila.SUCCESS) {
                        Trc20ContractModel trc20Contract = mTrc20ContractDao.findByContractAddress(contractAddress);

                        if (trc20Contract == null) {
                            trc20Contract = Trc20ContractModel.builder()
                                    .name(name)
                                    .symbol(symbol)
                                    .address(contractAddress)
                                    .precision(precision)
                                    .isFavorite(false)
                                    .build();

                            mTrc20ContractDao.insert(trc20Contract);
                        }
                    }

                    return result;
                })
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(result -> mView.resultAddTrc20(result), e -> {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                    mView.connectionError();
                });
    }

    public void syncTrc20TokenContracts() {
        mView.showSyncTrc20Loading();

        mStabilaNetwork.getTrc20TokenList()
                .subscribeOn(mRxJavaSchedulers.getIo())
                .map(trc20Tokens -> {
                    List<Trc20ContractModel> savedTokens = mTrc20ContractDao.getAll();

                    List<Trc20Token> newTokens = trc20Tokens.getTrc20TokenList();

                    List<Trc20Token> addTokens = new ArrayList<>();

                    for (Trc20Token newToken : newTokens) {
                        boolean isAdd = true;

                        for (Trc20ContractModel savedToken : savedTokens) {
                            if (newToken.getContractAddress().equalsIgnoreCase(savedToken.getAddress())) {
                                isAdd = false;
                                break;
                            }
                        }

                        if (isAdd) {
                            addTokens.add(newToken);
                        }
                    }

                    List<Trc20ContractModel> addModels = new ArrayList<>();

                    for (Trc20Token addToken : addTokens) {
                        addModels.add(Trc20ContractModel.builder()
                                .name(addToken.getName())
                                .symbol(addToken.getSymbol())
                                .address(addToken.getContractAddress())
                                .precision(addToken.getPrecision())
                                .isFavorite(false)
                                .build());
                    }

                    if (!addModels.isEmpty()) {
                        mTrc20ContractDao.insertAll(addModels);
                    }

                    return true;
                })
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(result -> {
                    mView.finishSyncTrc20();
                }, e -> {
                    e.printStackTrace();
                    mView.finishSyncTrc20();
                });
    }
}
