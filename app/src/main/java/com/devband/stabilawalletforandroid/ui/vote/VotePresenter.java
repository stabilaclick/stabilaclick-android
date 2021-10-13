package com.devband.stabilawalletforandroid.ui.vote;

import android.support.annotation.NonNull;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.ui.vote.dto.VoteItem;
import com.devband.stabilawalletforandroid.ui.vote.dto.VoteItemList;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.Witness;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.AccountManager;
import com.devband.stabilawalletforandroid.stabila.WalletAppManager;

import org.stabila.protos.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class VotePresenter extends BasePresenter<VoteView> {

    private AdapterDataModel<VoteItem> mAdapterDataModel;

    private List<VoteItem> mAllVotes;

    private List<VoteItem> mMyVotes;
    private Stabila mStabila;
    private StabilaNetwork mStabilaNetwork;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public VotePresenter(VoteView view, Stabila stabila, StabilaNetwork stabilaNetwork, WalletAppManager walletAppManager,
                         RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
        this.mStabilaNetwork = stabilaNetwork;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<VoteItem> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        getRepresentativeList(false);
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

    public void getRepresentativeList(boolean isMyVotes) {
        mView.showLoadingDialog();

        Single.zip(mStabilaNetwork.getVoteWitnesses(), mStabila.queryAccount(mStabila.getLoginAddress()), (votes, myAccount) -> {
            List<VoteItem> representatives = new ArrayList<>();

            int cnt = votes.getData().size();

            long totalMyVotes = 0;

            for (int i = 0; i < cnt; i++) {
                Witness witness = votes.getData().get(i);

                long myVoteCount = 0;

                for (Protocol.Vote vote : myAccount.getVotesList()) {
                    if (AccountManager.encode58Check(vote.getVoteAddress().toByteArray()).equals(witness.getAddress())) {
                        myVoteCount = vote.getVoteCount();
                        totalMyVotes += myVoteCount;
                        break;
                    }
                }

                representatives.add(VoteItem.builder()
                        .address(witness.getAddress())
                        .url(witness.getUrl())
                        .totalVoteCount(votes.getTotalVotes())
                        .lastCycleVoteCount(witness.getLastCycleVotes())
                        .realTimeVoteCount(witness.getRealTimeVotes())
                        .hasTeamPage(witness.isHasPage())
                        .votesPercentage(witness.getVotesPercentage())
                        .changeVotes(witness.getChangeVotes())
                        .myVoteCount(myVoteCount)
                        .build());
            }

            Descending descending = new Descending();
            Collections.sort(representatives, descending);

            for (int i = 0; i < cnt; i++) {
                VoteItem representative = representatives.get(i);
                representative.setNo(i + 1);
                representative.setTotalVoteCount(votes.getTotalVotes());
            }

            long myVotePoint = 0;

            for (Protocol.Account.Cded cded : myAccount.getCdedList()) {
                myVotePoint += cded.getCdedBalance();
            }

            myVotePoint = (long) (myVotePoint / Constants.ONE_STB);

            mAllVotes = new ArrayList<>();
            mMyVotes = new ArrayList<>();
            mAllVotes.addAll(representatives);

            for (int i = 0; i < mAllVotes.size(); i++) {
                VoteItem voteItem = mAllVotes.get(i);

                if (voteItem.getMyVoteCount() > 0) {
                    mMyVotes.add(voteItem);
                }
            }

            return VoteItemList.builder()
                    .voteItemList(representatives)
                    .voteItemCount(cnt)
                    .totalVotes(votes.getTotalVotes())
                    .totalMyVotes(totalMyVotes)
                    .myVotePoint(myVotePoint)
                    .build();
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(voteItemList -> {
            mView.displayVoteInfo(voteItemList.getTotalVotes(), voteItemList.getVoteItemCount(),
                    voteItemList.getMyVotePoint(), voteItemList.getTotalMyVotes());
            showOnlyMyVotes(isMyVotes);
        }, e -> mView.showServerError());
    }

    public void showOnlyMyVotes(boolean isMyVotes) {
        mAdapterDataModel.clear();

        if (isMyVotes) {
            mAdapterDataModel.addAll(mMyVotes);
        } else {
            mAdapterDataModel.addAll(mAllVotes);
        }

        mView.refreshList();
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void voteRepresentative(@NonNull String password, String address, long vote, boolean includeOtherVotes) {
        mView.showLoadingDialog();

        Single.fromCallable(() -> {
            Map<String, String> witness = new HashMap<>();

            long totalVote = 0;

            if (includeOtherVotes) {
                for (int i = 0; i < mMyVotes.size(); i++) {
                    VoteItem voteItem = mMyVotes.get(i);

                    if (!address.equalsIgnoreCase(voteItem.getAddress())) {
                        witness.put(voteItem.getAddress(), String.valueOf(voteItem.getMyVoteCount()));
                        totalVote += voteItem.getMyVoteCount();
                    }
                }
            }

            if (vote != 0) {
                witness.put(address, String.valueOf(vote));
                totalVote += vote;
            }

            if (totalVote < 1) {
                throw new IllegalStateException();
            }

            return witness;
        })
        .flatMap(witness -> mStabila.voteWitness(password, witness))
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successVote();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (e instanceof IllegalStateException) {
                    mView.showInvalidVoteError();
                } else {
                    mView.showServerError();
                }
            }
        });
    }

    class Descending implements Comparator<VoteItem> {

        @Override
        public int compare(VoteItem o1, VoteItem o2) {
            return o2.getRealTimeVoteCount().compareTo(o1.getRealTimeVoteCount());
        }
    }
}
