package com.devband.stabilalib.services;

import com.devband.stabilalib.dto.TriggerRequest;
import com.devband.stabilalib.dto.TriggerResult;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StabilaGridService {

    @POST("wallet/triggersmartcontract")
    public Single<TriggerResult> triggerSmartContract(@Body TriggerRequest request);
}
