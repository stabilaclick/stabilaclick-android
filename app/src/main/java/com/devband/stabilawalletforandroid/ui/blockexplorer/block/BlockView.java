package com.devband.stabilawalletforandroid.ui.blockexplorer.block;


import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilalib.dto.Blocks;

/**
 * Created by user on 2018. 5. 25..
 */

public interface BlockView extends IView {
    void blockDataLoadSuccess(Blocks blocks, boolean added);
    void showLoadingDialog();
    void showServerError();
}
