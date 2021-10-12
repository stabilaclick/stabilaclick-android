package com.devband.stabilawalletforandroid.ui.backupaccount;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface BackupAccountView extends IView {

    void displayAccountInfo(@NonNull String address, @NonNull String privateKey);

    void startMainActivity();
}
