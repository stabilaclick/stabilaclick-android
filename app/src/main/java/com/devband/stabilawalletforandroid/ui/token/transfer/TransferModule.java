package com.devband.stabilawalletforandroid.ui.token.transfer;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TransferModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {TransferFragmentModule.class})
    public abstract TransferFragment contributeTransferFragment();
}