package com.devband.stabilawalletforandroid.ui.blockexplorer.overview;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.StabilaAccounts;

/**
 * Created by user on 2018. 5. 28..
 */

public interface OverviewView extends IView {

    void overviewDataLoadSuccess(StabilaAccounts topAddressAccounts);
    void showLoadingDialog();
    void showServerError();

}
