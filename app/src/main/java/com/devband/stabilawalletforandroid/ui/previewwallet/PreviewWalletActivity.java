package com.devband.stabilawalletforandroid.ui.previewwallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.ui.main.MainActivity;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.AdapterView;
import com.devband.stabilawalletforandroid.common.CommonActivity;
import com.devband.stabilawalletforandroid.database.model.AccountModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewWalletActivity extends CommonActivity implements PreviewWalletView {

    @Inject
    PreviewWalletPresenter mPreviewWalletPresenter;

    @Inject
    Stabila mStabila;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;

    private boolean mIsLoading;

    private PreviewWalletAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_wallet);

        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_preview_wallet);
        }

        mAdapter = new PreviewWalletAdapter(mStabila, PreviewWalletActivity.this, mOnItemClickListener);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mAdapterView = mAdapter;

        mPreviewWalletPresenter.setAdapterDataModel(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mPreviewWalletPresenter.onCreate();
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            mIsLoading = true;
            mSwipeRefreshLayout.setRefreshing(true);
            mPreviewWalletPresenter.refreshAccount();
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int pos = mRecyclerView.getChildLayoutPosition(v);
            AccountModel item = mAdapter.getItem(pos);

            mStabila.changeLoginAccount(item);
            startActivity(MainActivity.class);
            finishActivity();
        }
    };

    @Override
    public void finishRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
