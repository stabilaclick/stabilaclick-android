package com.devband.stabilawalletforandroid.ui.blockexplorer.account;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AccountModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {AccountFragmentModule.class})
    public abstract AccountFragment contributeAccountFragment();
}