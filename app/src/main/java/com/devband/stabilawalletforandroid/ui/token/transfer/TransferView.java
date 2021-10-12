package com.devband.stabilawalletforandroid.ui.token.transfer;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface TransferView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
