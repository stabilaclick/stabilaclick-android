package com.devband.stabilawalletforandroid.rxjava;

import io.reactivex.Scheduler;

public interface RxJavaSchedulers {

    Scheduler getNewThread();

    Scheduler getComputation();

    Scheduler getIo();

    Scheduler getMainThread();
}
