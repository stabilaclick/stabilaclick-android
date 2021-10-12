package com.devband.stabilawalletforandroid.ui.accountdetail.overview;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AccountVoteActivityModule {

    @Binds
    public abstract AccountVoteView view(AccountVoteActivity accountVoteActivity);

    @Provides
    static AccountVotePresenter provideAccountVotePresenter(AccountVoteView view, StabilaNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new AccountVotePresenter(view, tronNetwork, rxJavaSchedulers);
    }
}
