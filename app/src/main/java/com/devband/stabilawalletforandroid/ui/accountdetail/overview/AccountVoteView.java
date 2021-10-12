package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface AccountVoteView extends IView {

    void finishLoading(long totalVotes, long total);
    void showLoadingDialog();
    void showServerError();
}
