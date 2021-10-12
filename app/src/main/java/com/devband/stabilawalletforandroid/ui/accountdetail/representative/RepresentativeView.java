package com.devband.stabilawalletforandroid.ui.accountdetail.representative;

import com.devband.stabilawalletforandroid.ui.mvp.IView;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.model.BaseModel;

import java.util.List;

public interface RepresentativeView extends IView {
    void dataLoadSuccess(List<BaseModel> viewModels);
    void showLoadingDialog();
    void showServerError();
}
