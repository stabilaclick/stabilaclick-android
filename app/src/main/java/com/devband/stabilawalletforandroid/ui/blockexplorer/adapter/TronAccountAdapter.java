package com.devband.stabilawalletforandroid.ui.blockexplorer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.stabilalib.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.AdapterDataModel;
import com.devband.stabilawalletforandroid.common.AdapterView;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.common.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TronAccountAdapter extends RecyclerView.Adapter<TronAccountAdapter.TokenHolderViewHolder> implements AdapterDataModel<StabilaAccount>, AdapterView {

    private List<StabilaAccount> mList;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public TronAccountAdapter(Context context, View.OnClickListener onItemClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TokenHolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stabila_account, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TokenHolderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenHolderViewHolder holder, int position) {
        StabilaAccount item = mList.get(position);

        holder.stabilaAddressNoText.setText((position + 1) + ".");
        Utils.setAccountDetailAction(mContext, holder.stabilaAddressText, item.getAddress());
        holder.stabilaBalanceText.setText(Constants.tokenBalanceFormat.format(item.getBalance() / Constants.ONE_STB) + " " + Constants.TRON_SYMBOL);
        holder.stabilaBalancePercentText.setText(Constants.percentFormat.format(item.getBalancePercent()) + "%");
        holder.stabilaBalanceProgress.setMax((float) item.getAvailableSypply());
        holder.stabilaBalanceProgress.setProgress((float) ((double) item.getBalance() / Constants.ONE_STB));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(StabilaAccount model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<StabilaAccount> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public StabilaAccount getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    class TokenHolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stabila_address_no_text)
        public TextView stabilaAddressNoText;

        @BindView(R.id.stabila_address_text)
        public TextView stabilaAddressText;

        @BindView(R.id.stabila_balance_text)
        public TextView stabilaBalanceText;

        @BindView(R.id.stabila_balance_percent_text)
        public TextView stabilaBalancePercentText;

        @BindView(R.id.progress_balance)
        public RoundCornerProgressBar stabilaBalanceProgress;

        public TokenHolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
