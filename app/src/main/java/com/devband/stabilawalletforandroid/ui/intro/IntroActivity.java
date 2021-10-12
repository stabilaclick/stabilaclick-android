package com.devband.stabilawalletforandroid.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.devband.stabilawalletforandroid.ui.login.LoginActivity;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.CommonActivity;
import com.devband.stabilawalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.stabilawalletforandroid.ui.createwallet.CreateWalletActivity;

import javax.inject.Inject;

public class IntroActivity extends CommonActivity implements IntroView {

    @Inject
    IntroPresenter mIntroPresenter;

    private boolean mIsBackClick = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mIntroPresenter.onCreate();
    }

    @Override
    public void startCreateAccountActivity() {
        if (!mIsBackClick) {
            startActivity(CreateWalletActivity.class);
        }
        finishActivity();
    }

    @Override
    public void startLoginActivity() {
        if (!mIsBackClick) {
            startActivity(LoginActivity.class);
        }
        finishActivity();
    }

    @Override
    public void startBackupAccountActivity() {
        if (!mIsBackClick) {
            startActivity(BackupAccountActivity.class);
        }
        finishActivity();
    }

    @Override
    public void showErrorMsg() {
        if (!isFinishing()) {
            Toast.makeText(IntroActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void doesNotSupportAlgorithm() {
        if (!isFinishing()) {
            Toast.makeText(IntroActivity.this, getString(R.string.does_not_support_algorithm_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void connectionError() {
        if (!isFinishing()) {
            Toast.makeText(IntroActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
            startLoginActivity();
        }
    }

    @Override
    public void onBackPressed() {
        mIsBackClick = true;

        super.onBackPressed();
    }
}
