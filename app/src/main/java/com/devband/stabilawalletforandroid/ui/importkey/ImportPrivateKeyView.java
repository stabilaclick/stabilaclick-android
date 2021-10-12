package com.devband.stabilawalletforandroid.ui.importkey;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface ImportPrivateKeyView extends IView {

    void createdWallet();

    void passwordError();

    void registerWalletError();
}
