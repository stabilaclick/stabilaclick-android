package com.devband.stabilawalletforandroid.ui.blockexplorer.overview;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.TronAccounts;

/**
 * Created by user on 2018. 5. 28..
 */

public interface OverviewView extends IView {

    void overviewDataLoadSuccess(TronAccounts topAddressAccounts);
    void showLoadingDialog();
    void showServerError();

}
