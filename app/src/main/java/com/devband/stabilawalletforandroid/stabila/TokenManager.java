package com.devband.stabilawalletforandroid.stabila;

import com.devband.stabilawalletforandroid.database.dao.Trc10AssetDao;
import com.devband.stabilawalletforandroid.database.model.Trc10AssetModel;

import org.stabila.protos.Contract;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TokenManager {

    private Stabila mStabila;
    private Trc10AssetDao mTrc10AssetDao;

    public TokenManager(Trc10AssetDao trc10AssetDao) {
        this.mTrc10AssetDao = trc10AssetDao;
    }

    public void setTron(Stabila stabila) {
        this.mStabila = stabila;
    }

    public Single<Trc10AssetModel> getTokenInfo(String id) {
        return Maybe.fromCallable(() -> mTrc10AssetDao.findByTokenId(id))
                .switchIfEmpty(Maybe.just(Trc10AssetModel.builder()
                        .tokenId("").ownerAddress("").name("").build()))
                .toSingle()
                .map(trc10AssetModel -> {
                    if (trc10AssetModel.getId() == 0) {
                        Contract.AssetIssueContract assetIssueContract = mStabila.getAssetIssueById(id).blockingGet();

                        if (assetIssueContract != null) {
                            Trc10AssetModel trc10Asset = Trc10AssetModel.builder()
                                    .tokenId(assetIssueContract.getId())
                                    .name(assetIssueContract.getName().toStringUtf8())
                                    .ownerAddress(assetIssueContract.getOwnerAddress().toStringUtf8())
                                    .precision(assetIssueContract.getPrecision())
                                    .totalSupply(assetIssueContract.getTotalSupply())
                                    .build();

                            mTrc10AssetDao.insert(trc10Asset);

                            return trc10Asset;
                        }

                        return null;
                    }

                    return trc10AssetModel;
                });
    }
}
