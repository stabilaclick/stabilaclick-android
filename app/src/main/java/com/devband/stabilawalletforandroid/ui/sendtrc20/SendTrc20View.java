package com.devband.stabilawalletforandroid.ui.sendtrc20;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilawalletforandroid.database.model.Trc20ContractModel;

import java.util.List;

public interface SendTrc20View extends IView {

    void showLoadingDialog();
    void setTrc20List(List<Trc20ContractModel> trc20ContractModels);

    void success();
    void failed();
    void invalidPassword();
}
