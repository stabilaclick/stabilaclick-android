package com.devband.stabilawalletforandroid.ui.representative;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface RepresentativeView extends IView {

    void showLoadingDialog();

    void displayRepresentativeInfo(int witnessCount, long highestVotes);

    void showServerError();
}
