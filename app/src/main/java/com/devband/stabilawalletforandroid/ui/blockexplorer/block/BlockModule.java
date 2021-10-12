package com.devband.stabilawalletforandroid.ui.blockexplorer.block;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BlockModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {BlockFragmentModule.class})
    public abstract BlockFragment contributeBlockFragment();
}