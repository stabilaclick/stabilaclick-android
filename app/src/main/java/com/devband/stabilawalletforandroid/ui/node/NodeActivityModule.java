package com.devband.stabilawalletforandroid.ui.node;

import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class NodeActivityModule {

    @Binds
    public abstract NodeView view(NodeActivity nodeActivity);

    @Provides
    static NodePresenter provideNodePresenter(NodeView view, Stabila tron, RxJavaSchedulers rxJavaSchedulers) {
        return new NodePresenter(view, tron, rxJavaSchedulers);
    }
}
