package com.devband.stabilawalletforandroid.ui.vote;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface VoteView extends IView {

    void showLoadingDialog();

    void showServerError();

    void displayVoteInfo(long totalVotes, long voteItemCount, long myVotePoint, long totalMyVotes);

    void successVote();

    void refreshList();

    void showInvalidVoteError();
}
