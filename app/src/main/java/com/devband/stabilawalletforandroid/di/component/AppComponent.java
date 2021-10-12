package com.devband.stabilawalletforandroid.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;
import com.devband.stabilawalletforandroid.di.ActivityBindingModule;
import com.devband.stabilawalletforandroid.di.module.AppModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBindingModule.class
})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

    void inject(DaggerApplication application);
}