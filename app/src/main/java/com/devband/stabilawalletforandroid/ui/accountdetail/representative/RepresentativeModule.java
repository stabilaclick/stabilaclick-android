package com.devband.stabilawalletforandroid.ui.accountdetail.representative;

import com.devband.stabilawalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RepresentativeModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {RepresentativeFragmentModule.class})
    public abstract RepresentativeFragment contributeRepresentativeFragment();
}