package com.devband.stabilalib.services;

import com.devband.stabilalib.dto.javastabila.AssetIssueList;
import com.devband.stabilalib.dto.javastabila.PaginatedRequest;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JavaStabilaHttpService {

    @POST("wallet/getpaginatedassetissuelist")
    Single<AssetIssueList> getPaginatedAssetIssueList(@Body PaginatedRequest paginatedRequest);
}
