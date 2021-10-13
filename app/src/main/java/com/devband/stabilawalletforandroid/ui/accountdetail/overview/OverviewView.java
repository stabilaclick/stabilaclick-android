package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface OverviewView extends IView {

    void showLoadingDialog();
    void showServerError();
    void finishLoading(@NonNull StabilaAccount account);
}
