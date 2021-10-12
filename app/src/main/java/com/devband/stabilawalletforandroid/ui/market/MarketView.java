package com.devband.stabilawalletforandroid.ui.market;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.Market;

import java.util.List;

/**
 * Created by user on 2018. 5. 24..
 */

public interface MarketView extends IView {
    void marketDataLoadSuccess(List<Market> markets);
    void showLoadingDialog();
    void showServerError();
}
