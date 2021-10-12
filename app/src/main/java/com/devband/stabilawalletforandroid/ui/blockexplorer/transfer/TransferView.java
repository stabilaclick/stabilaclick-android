package com.devband.stabilawalletforandroid.ui.blockexplorer.transfer;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface TransferView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
