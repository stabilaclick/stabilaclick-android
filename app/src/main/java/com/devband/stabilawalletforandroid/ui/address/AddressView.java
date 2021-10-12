package com.devband.stabilawalletforandroid.ui.address;

import android.support.annotation.Nullable;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface AddressView extends IView {

    void addressResult(@Nullable AddressPresenter.AddressInfo addressInfo);

}
