package com.devband.stabilawalletforandroid.ui.intro;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

interface IntroView extends IView {

    void startCreateAccountActivity();

    void startLoginActivity();

    void startBackupAccountActivity();

    void showErrorMsg();

    void doesNotSupportAlgorithm();

    void connectionError();
}
