package com.devband.stabilawalletforandroid.ui.more;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MoreActivityModule {

    @Binds
    public abstract MoreView view(MoreActivity moreActivity);

    @Provides
    static MorePresenter provideMorePresenter(MoreView moreView) {
        return new MorePresenter(moreView);
    }
}
