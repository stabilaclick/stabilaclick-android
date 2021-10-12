package com.devband.stabilawalletforandroid.ui.token.holder;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface HolderView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
