package com.devband.stabilawalletforandroid.ui.blockexplorer.block;

import android.util.Log;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;

/**
 * Created by user on 2018. 5. 25..
 */

public class BlockPresenter extends BasePresenter<BlockView> {

    private static final int DEFAULT_LIMIT = 15;
    private int mLimit = DEFAULT_LIMIT;
    private int mStart = 0;
    private StabilaNetwork mStabilaNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public BlockPresenter(BlockView view, StabilaNetwork stabilaNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabilaNetwork = stabilaNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

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

    public void loadBlockData() {
        mView.showLoadingDialog();

        mStabilaNetwork.getBlocks(mLimit, mStart)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        blocks -> {
                            mView.blockDataLoadSuccess(blocks, mStart != 0);
                            mStart += mLimit;
                        }, t -> {
                            Log.d("hanseon--", "loadBlockData fail : " + t.getMessage());
                            mView.showServerError();
                        }
                );
    }
}
