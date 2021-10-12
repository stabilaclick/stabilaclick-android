package com.devband.stabilawalletforandroid.ui.token;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

import org.stabila.protos.Protocol;

public interface TokenView extends IView {

    void showLoadingDialog();

    void showServerError();

    void finishLoading(int total, Protocol.Account account);

    void participateTokenResult(boolean result);

    void needLogin();
}
