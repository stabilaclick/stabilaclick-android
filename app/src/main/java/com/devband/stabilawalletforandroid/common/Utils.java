package com.devband.stabilawalletforandroid.common;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.devband.stabilawalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.stabilawalletforandroid.ui.blockdetail.BlockDetailActivity;
import com.devband.stabilawalletforandroid.R;

import org.stabila.protos.Protocol;

import java.util.Date;

public class Utils {

    public static String getContractTypeString(Context context, int contractType) {

        String[] contractTypes = context.getResources().getStringArray(R.array.contract_types);

        try {
            if (contractType == Protocol.Transaction.Contract.ContractType.UNRECOGNIZED.getNumber()) {
                return context.getString(R.string.unrecognized_text);
            }
            return contractTypes[contractType];
        } catch (Exception e) {
            return context.getString(R.string.unrecognized_text);
        }
    }

    public static String getDateTimeWithTimezone(long timestamp) {
        return Constants.sdf.format(new Date(timestamp));
    }

    public static String getUsdFormat(float number) {
        return Constants.usdFormat.format(number);
    }

    public static String getUsdFormat(double number) {
        return Constants.usdFormat.format(number);
    }

    public static String getCommaNumber(int number) {
        return Constants.numberFormat.format(number);
    }

    public static String getCommaNumber(long number) {
        return Constants.numberFormat.format(number);
    }

    public static String getStbFormat(double number) {
        return Constants.tokenBalanceFormat.format(number);
    }

    public static String getRealStbFormat(long number) {
        return Constants.tokenBalanceFormat.format((double) number / Constants.ONE_STB);
    }

    public static String getPercentFormat(float number) {
        return Constants.percentFormat.format(number);
    }

    public static String getPercentFormat(double number) {
        return Constants.percentFormat.format(number);
    }

    public static void setAccountDetailAction(Context context, TextView textView, String address) {
        SpannableString content = new SpannableString(address);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        textView.setText(content);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AccountDetailActivity.class);
            intent.putExtra(AccountDetailActivity.EXTRA_ADDRESS, address);
            context.startActivity(intent);
        });
    }

    public static void setBlockDetailAction(Context context, TextView textView, long blockNum) {
        SpannableString content = new SpannableString(Constants.numberFormat.format(blockNum));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        textView.setText(content);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BlockDetailActivity.class);
            intent.putExtra(BlockDetailActivity.EXTRA_BLOCK_NUMBER, blockNum);
            context.startActivity(intent);
        });
    }
}