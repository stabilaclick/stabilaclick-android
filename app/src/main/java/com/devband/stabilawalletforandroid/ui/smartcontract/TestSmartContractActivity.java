package com.devband.stabilawalletforandroid.ui.smartcontract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.devband.stabilalib.dto.TriggerRequest;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.CommonActivity;
import com.devband.stabilawalletforandroid.stabila.AccountManager;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import org.spongycastle.util.encoders.Hex;
import org.stabila.common.utils.AbiUtil;
import org.stabila.common.utils.ByteArray;
import org.stabila.core.exception.EncodingException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestSmartContractActivity extends CommonActivity {

    @Inject
    Stabila mStabila;

    @BindView(R.id.contract_address_edit)
    EditText contractAddressEdit;

    @BindView(R.id.abi_text)
    TextView abiText;

    @BindView(R.id.method_edit)
    EditText contractMethod;

    @BindView(R.id.params_edit)
    EditText contractParams;

    @BindView(R.id.fee_limit_edit)
    EditText feeLimit;

    @BindView(R.id.call_value_edit)
    EditText callValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_contract);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_abi_button)
    public void onGetAbiClick() {
        this.mStabila.getSmartContract(contractAddressEdit.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contract -> abiText.setText(contract.getAbi().toString()), e -> e.printStackTrace());
    }

    @OnClick(R.id.send_transaction_button)
    public void onSendTransactionClick() {
        Single.fromCallable(() -> {
            String transferMethod = contractMethod.getText().toString();
            //String transferParams = contractParams.getText().toString();

            String transferParams = "\"address\",1000000";

            String contractTrigger = "";

            try {
                contractTrigger = AbiUtil.parseMethod(transferMethod, transferParams);
            } catch (EncodingException e ) {
                e.printStackTrace();
            }

            byte[] input = Hex.decode(contractTrigger);
            byte[] contractAddress = AccountManager.decodeFromBase58Check(contractAddressEdit.getText().toString());

            TriggerRequest triggerRequest = TriggerRequest.builder()
                    .contractAddress(ByteArray.toHexString(contractAddress))
                    .ownerAddress(ByteArray.toHexString(AccountManager.decodeFromBase58Check(mStabila.getLoginAddress())))
                    .functionSelector(transferMethod)
                    .parameter(contractTrigger)
                    .callValue(Long.parseLong(callValue.getText().toString()))
                    .feeLimit(Long.parseLong(feeLimit.getText().toString()))
                    .build();

            //TriggerResult result = mStabilaGridService.triggerSmartContract(triggerRequest).blockingGet();
            return mStabila.callQueryContract(mStabila.getLoginAddress(), contractAddress, Long.parseLong(callValue.getText().toString()), input, Long.parseLong(feeLimit.getText().toString()), 0L, null);
        })
        .flatMap(result -> result)
        .subscribe(result -> {
            Timber.d("result : " + result);
        }, e -> e.printStackTrace());
    }
}
