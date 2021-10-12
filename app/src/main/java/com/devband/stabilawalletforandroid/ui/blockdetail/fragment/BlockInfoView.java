package com.devband.stabilawalletforandroid.ui.blockdetail.fragment;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.Block;

public interface BlockInfoView extends IView {
    void showLoadingDialog();
    void showServerError();
    void finishLoading(@NonNull Block block);
}
