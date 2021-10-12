package com.devband.stabilawalletforandroid.ui.blockexplorer.account;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface AccountView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
