package com.devband.stabilawalletforandroid.ui.previewwallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.stabilawalletforandroid.stabila.Stabila;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.AdapterView;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.database.model.AccountModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PreviewWalletAdapter extends RecyclerView.Adapter<PreviewWalletAdapter.AccountViewHolder> implements AdapterDataModel<AccountModel>, AdapterView {

    private List<AccountModel> mList = new ArrayList<>();

    private Stabila mStabila;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public PreviewWalletAdapter(Stabila stabila, Context context, View.OnClickListener onClickListener) {
        this.mStabila = stabila;
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_preview_wallet, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new PreviewWalletAdapter.AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountModel wallet = mList.get(position);

        holder.walletNameText.setText(wallet.getName());

        // get balance
        mStabila.getEncryptAccount(wallet.getAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(account -> {
                    holder.balanceText.setText(Constants.tokenBalanceFormat.format(account.getBalance() / Constants.ONE_STB) + " " + Constants.TRON_SYMBOL);
                }, e -> e.printStackTrace());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(AccountModel model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<AccountModel> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public AccountModel getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public AccountModel getItem(int pos) {
        return mList.get(pos);
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wallet_name_text)
        public TextView walletNameText;

        @BindView(R.id.balance_text)
        public TextView balanceText;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
