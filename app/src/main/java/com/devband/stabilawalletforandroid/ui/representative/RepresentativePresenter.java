package com.devband.stabilawalletforandroid.ui.representative;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.stabila.AccountManager;
import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.ui.representative.dto.Representative;
import com.devband.stabilawalletforandroid.ui.representative.dto.RepresentativeList;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import org.stabila.protos.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class RepresentativePresenter extends BasePresenter<RepresentativeView> {

    private AdapterDataModel<Representative> mAdapterDataModel;
    private Stabila mStabila;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public RepresentativePresenter(RepresentativeView view, Stabila stabila, WalletAppManager walletAppManager,
                                   RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<Representative> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        mView.showLoadingDialog();
        getRepresentativeList();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void getRepresentativeList() {
        mStabila.getWitnessList()
        .map(witnessList -> {
            List<Representative> representatives = new ArrayList<>();

            int cnt = witnessList.getWitnessesCount();

            long highestVotes = 0;

            for (int i = 0; i < cnt; i++) {
                Protocol.Witness witness = witnessList.getWitnesses(i);

                representatives.add(Representative.builder()
                        .address(AccountManager.encode58Check(witness.getAddress().toByteArray()))
                        .url(witness.getUrl())
                        .voteCount(witness.getVoteCount())
                        .latestBlockNum(witness.getLatestBlockNum())
                        .totalProduced(witness.getTotalProduced())
                        .totalMissed(witness.getTotalMissed())
                        .productivity(((double) witness.getTotalProduced()) / (witness.getTotalProduced() + witness.getTotalMissed()))
                        .build());

                if (witness.getVoteCount() > highestVotes) {
                    highestVotes = witness.getVoteCount();
                }
            }

            Descending descending = new Descending();
            Collections.sort(representatives, descending);

            return RepresentativeList.builder()
                    .representativeList(representatives)
                    .representativeCount(cnt)
                    .highestVotes(highestVotes)
                    .build();
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<RepresentativeList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(RepresentativeList representativeList) {
                mView.displayRepresentativeInfo(representativeList.getRepresentativeCount(), representativeList.getHighestVotes());
                mAdapterDataModel.addAll(representativeList.getRepresentativeList());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    class Descending implements Comparator<Representative> {

        @Override
        public int compare(Representative o1, Representative o2) {
            return o2.getVoteCount().compareTo(o1.getVoteCount());
        }
    }
}
