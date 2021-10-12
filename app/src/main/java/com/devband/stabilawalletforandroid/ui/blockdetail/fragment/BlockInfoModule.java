package com.devband.stabilawalletforandroid.ui.blockdetail.fragment;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BlockInfoModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {BlockInfoFragmentModule.class})
    public abstract BlockInfoFragment contributeBlockInfoFragment();
}