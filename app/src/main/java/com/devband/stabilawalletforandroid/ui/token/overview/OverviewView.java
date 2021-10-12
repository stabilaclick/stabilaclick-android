package com.devband.stabilawalletforandroid.ui.token.overview;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.Token;

public interface OverviewView extends IView {

    void tokenInfoLoadSuccess(@NonNull Token token);
    void showLoadingDialog();
    void showServerError();
}
