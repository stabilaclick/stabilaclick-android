package com.devband.stabilawalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface MyAccountView extends IView {

    void displayAccountInfo(@NonNull String address, @Nullable StabilaAccount account);

    void showLoadingDialog();

    void hideDialog();

    void showServerError();

    void successCdBalance();

    void unableToUncd();

    void showInvalidPasswordMsg();

    void successDelete();
}
