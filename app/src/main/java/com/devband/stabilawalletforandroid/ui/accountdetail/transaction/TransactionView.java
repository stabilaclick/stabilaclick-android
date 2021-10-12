package com.devband.stabilawalletforandroid.ui.accountdetail.transaction;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface TransactionView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
