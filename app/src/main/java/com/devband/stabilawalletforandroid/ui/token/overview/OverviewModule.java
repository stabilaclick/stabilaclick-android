package com.devband.stabilawalletforandroid.ui.token.overview;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OverviewModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {OverviewFragmentModule.class})
    public abstract OverviewFragment contributeOverviewFragment();
}