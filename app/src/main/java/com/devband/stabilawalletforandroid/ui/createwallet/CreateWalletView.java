package com.devband.stabilawalletforandroid.ui.createwallet;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface CreateWalletView extends IView {

    void createdWallet(@NonNull byte[] aesKey);

    void passwordError();

    void registerWalletError();
}
