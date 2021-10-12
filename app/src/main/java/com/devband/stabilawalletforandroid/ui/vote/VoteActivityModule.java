package com.devband.stabilawalletforandroid.ui.vote;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class VoteActivityModule {

    @Binds
    public abstract VoteView view(VoteActivity voteActivity);

    @Provides
    static VotePresenter provideVotePresenter(VoteView view, Stabila tron, StabilaNetwork tronNetwork,
                                              WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new VotePresenter(view, tron, tronNetwork, walletAppManager, rxJavaSchedulers);
    }
}
