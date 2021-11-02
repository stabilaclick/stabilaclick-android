package com.devband.stabilawalletforandroid.ui.main;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.CoinMarketCap;

public interface MainView extends IView {

    void showLoadingDialog();

    void displayAccountInfo(@NonNull StabilaAccount account);

    void setStabilaMarketInfo(CoinMarketCap coinMarketCap);

    void showInvalidPasswordMsg();

    void successCreateAccount();

    void successImportAccount();

    void failCreateAccount();

    void duplicatedAccount();

    void connectionError();

    void changePasswordResult(boolean result);

    void showChangePasswordDialog();

    void goToIntroActivity();

    void resultAddTrc20(int result);

    void finishSyncTrc20();

    void showSyncTrc20Loading();
}
