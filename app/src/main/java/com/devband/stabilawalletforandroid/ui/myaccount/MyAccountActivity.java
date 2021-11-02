package com.devband.stabilawalletforandroid.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.stabilawalletforandroid.ui.address.AddressActivity;
import com.devband.stabilawalletforandroid.ui.main.dto.Asset;
import com.devband.stabilawalletforandroid.ui.main.dto.Cded;
import com.devband.stabilawalletforandroid.ui.main.dto.StabilaAccount;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.CommonActivity;
import com.devband.stabilawalletforandroid.common.Constants;
import com.devband.stabilawalletforandroid.database.model.AccountModel;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyAccountActivity extends CommonActivity implements MyAccountView {

    @Inject
    MyAccountPresenter mMyAccountPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.account_spinner)
    Spinner mAccountSpinner;

    @BindView(R.id.name_text)
    TextView mNameText;

    @BindView(R.id.address_text)
    TextView mAddressText;

    @BindView(R.id.balance_text)
    TextView mBalanceText;

    @BindView(R.id.stabila_power_text)
    TextView mStabilaPowerText;

    @BindView(R.id.bandwidth_text)
    TextView mBandwidthText;

    @BindView(R.id.cd_button)
    Button mCdButton;

    @BindView(R.id.uncd_button)
    Button mUnCdButton;

    @BindView(R.id.cded_trx_balance_text)
    TextView mCdedStbBalanceText;

    @BindView(R.id.cded_trx_expired_text)
    TextView mCdedStbExpiredText;

    @BindView(R.id.tokens_layout)
    LinearLayout mTokensLayout;

    private ArrayAdapter<AccountModel> mAccountAdapter;

    private long mAccountBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_my_account);
        }

        mMyAccountPresenter.onCreate();

        initAccountList();
    }

    private void initAccountList() {
        mMyAccountPresenter.getAccountList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountModelList -> {
                    mAccountAdapter = new ArrayAdapter<>(MyAccountActivity.this, android.R.layout.simple_spinner_item,
                            accountModelList);

                    mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAccountSpinner.setAdapter(mAccountAdapter);

                    for (int i = 0; i < accountModelList.size(); i++) {
                        long id = mMyAccountPresenter.getLoginAccountIndex();
                        if (id == accountModelList.get(i).getId()) {
                            mAccountSpinner.setSelection(i);
                            break;
                        }
                    }

                    mAccountSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
                }, e -> {});
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyAccountPresenter.onResume();
    }

    @Override
    public void displayAccountInfo(@NonNull String address, @Nullable StabilaAccount account) {
        if (isFinishing()) {
            return;
        }

        mAddressText.setText(address);

        if (account != null) {
            mAccountBalance = (long) (account.getBalance() / Constants.ONE_STB);

            if (TextUtils.isEmpty(account.getName())) {
                mNameText.setText("-");
            } else {
                mNameText.setText(account.getName());
            }

            mBalanceText.setText(Constants.tokenBalanceFormat.format(mAccountBalance) + " " + Constants.STABILA_SYMBOL);
            mBandwidthText.setText(account.getBandwidthUsed() == 0 ? "-" : Constants.tokenBalanceFormat.format(account.getBandwidthUsed()));
            mTokensLayout.removeAllViews();

            if (!account.getAssetList().isEmpty()) {
                for (Asset asset : account.getAssetList()) {
                    View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_my_token, null);
                    v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT));

                    TextView tokenNameText = v.findViewById(R.id.token_name_text);
                    TextView tokenAmountText = v.findViewById(R.id.token_amount_text);
                    ImageButton favoriteButton = v.findViewById(R.id.token_favorite_button);
                    favoriteButton.setVisibility(View.VISIBLE);
                    favoriteButton.setTag(asset);

                    if (mMyAccountPresenter.isFavoriteToken(asset.getName())) {
                        favoriteButton.setImageResource(R.drawable.ic_star);
                    } else {
                        favoriteButton.setImageResource(R.drawable.ic_star_outline);
                    }

                    favoriteButton.setOnClickListener(view -> {
                        if (view.getTag() instanceof Asset) {
                            Asset tag = (Asset) view.getTag();

                            if (mMyAccountPresenter.isFavoriteToken(tag.getName())) {
                                mMyAccountPresenter.removeFavorite(tag.getName());
                                favoriteButton.setImageResource(R.drawable.ic_star_outline);
                            } else {
                                mMyAccountPresenter.doFavorite(tag.getName());
                                favoriteButton.setImageResource(R.drawable.ic_star);
                            }
                        }
                    });

                    tokenNameText.setText(asset.getDisplayName());
                    tokenAmountText.setText(Constants.tokenBalanceFormat.format(asset.getBalance()));
                    mTokensLayout.addView(v);
                }
            } else {
                View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_my_token, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));

                TextView tokenNameText = v.findViewById(R.id.token_name_text);
                TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

                tokenNameText.setText(getString(R.string.no_tokens));
                tokenNameText.setGravity(Gravity.CENTER);
                tokenAmountText.setVisibility(View.GONE);
                mTokensLayout.addView(v);
            }

            mCdButton.setVisibility(View.VISIBLE);

            long cdedBalance = 0;
            long expiredTime = 0;

            if (!account.getCdedList().isEmpty()) {
                for (Cded cded : account.getCdedList()) {
                    cdedBalance += cded.getCdedBalance();
                    if (cded.getExpireTime() > expiredTime) {
                        expiredTime = cded.getExpireTime();
                    }
                }

                mUnCdButton.setVisibility(View.VISIBLE);
            } else {
                mUnCdButton.setVisibility(View.GONE);
            }

            mStabilaPowerText.setText(Constants.tokenBalanceFormat.format(cdedBalance / Constants.ONE_STB) + " " + Constants.STABILA_SYMBOL);
            mCdedStbBalanceText.setText(Constants.tokenBalanceFormat.format(cdedBalance / Constants.ONE_STB) + " " + Constants.STABILA_SYMBOL);
            if (expiredTime > 0) {
                mCdedStbExpiredText.setText(Constants.sdf.format(new Date(expiredTime)));
            } else {
                mCdedStbExpiredText.setText("-");
            }
        }

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successCdBalance() {
        mMyAccountPresenter.getAccountAccountInfo();
    }

    @Override
    public void unableToUncd() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.unable_to_uncd_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidPasswordMsg() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successDelete() {
        hideDialog();

        initAccountList();
    }

    @OnClick(R.id.btn_remove_account)
    public void onRemoveAccountClick() {
        if (mMyAccountPresenter.getAccountCount() < 2) {
            Toast.makeText(MyAccountActivity.this, getString(R.string.remove_account_error_msg),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_remove_acocunt)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_remove_account, false);

        MaterialDialog dialog = builder.build();

        Button removeButton = (Button) dialog.getCustomView().findViewById(R.id.btn_remove_account);
        TextView accountNameText = (TextView) dialog.getCustomView().findViewById(R.id.account_name_text);
        EditText accountNameInput = (EditText) dialog.getCustomView().findViewById(R.id.input_account_name);

        removeButton.setEnabled(false);

        final AccountModel loginAccount = mMyAccountPresenter.getLoginAccount();

        addDisposable(RxTextView.textChanges(accountNameInput)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountName -> {
                    if (loginAccount.getName().equals(accountName)) {
                        removeButton.setBackgroundResource(R.color.colorAccent);
                        removeButton.setEnabled(true);
                    } else {
                        removeButton.setBackgroundResource(R.color.copy_address_button_color);
                        removeButton.setEnabled(false);
                    }
                }));

        accountNameText.setText(loginAccount.getName());

        removeButton.setOnClickListener(v -> {
            if (loginAccount.getName().equals(accountNameInput.getText().toString())) {
                mMyAccountPresenter.removeAccount(loginAccount.getId(), accountNameInput.getText().toString());
            }
        });

        dialog.show();
    }

    @OnClick(R.id.btn_export_private_key)
    public void onExportPrivateKeyClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_export_private_key)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.input_password_text), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String password = input.toString();

                        if (!TextUtils.isEmpty(password) && mMyAccountPresenter.matchPassword(password)) {
                            String privateKey = mMyAccountPresenter.getLoginPrivateKey(password);

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, privateKey);
                            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_private_key)));
                        } else {
                            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @OnClick(R.id.btn_copy_address)
    public void onCopyAddressClick() {
        startActivity(AddressActivity.class);
    }

    @OnClick(R.id.cd_button)
    public void onCdClick() {
        if (mAccountBalance == 0) {
            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_cd_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_cd_trx)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_cd_stb, false);

        MaterialDialog dialog = builder.build();

        Button maxButton = (Button) dialog.getCustomView().findViewById(R.id.max_button);
        Button cdButton = (Button) dialog.getCustomView().findViewById(R.id.btn_cd);
        CheckBox agreeCdCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.agree_cd_balance);
        EditText inputAmount = (EditText) dialog.getCustomView().findViewById(R.id.input_amount);
        EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);

        maxButton.setOnClickListener(view -> {
            inputAmount.setText(String.valueOf(mAccountBalance));
        });

        cdButton.setOnClickListener(view -> {
            // check cd balance
            long cdBalance = 0;
            try {
                cdBalance = Long.parseLong(inputAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_amount),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (cdBalance > mAccountBalance) {
                Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_amount),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String password = inputPassword.getText().toString();
            if (TextUtils.isEmpty(password) || !mMyAccountPresenter.matchPassword(password)) {
                Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean agree = agreeCdCheckBox.isChecked();

            if (!agree) {
                Toast.makeText(MyAccountActivity.this, getString(R.string.need_all_agree),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();
            mMyAccountPresenter.cdBalance(password, (long) (cdBalance * Constants.ONE_STB));
        });

        dialog.show();
    }

    @OnClick(R.id.uncd_button)
    public void onUnCdClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_uncd_trx)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.input_password_text), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String password = input.toString();

                        if (!TextUtils.isEmpty(password) && mMyAccountPresenter.matchPassword(password)) {
                            mMyAccountPresenter.uncdBalance(password);
                        } else {
                            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @OnClick(R.id.stabila_power_layout)
    public void onStabilaPowerHelpClick() {
        new MaterialDialog.Builder(MyAccountActivity.this)
                .title(getString(R.string.stabila_power_text))
                .content(getString(R.string.stabila_power_help_text))
                .titleColorRes(android.R.color.black)
                .contentColorRes(android.R.color.black)
                .backgroundColorRes(android.R.color.white)
                .autoDismiss(true)
                .build()
                .show();
    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            AccountModel accountModel = mAccountAdapter.getItem(pos);
            mMyAccountPresenter.changeLoginAccount(accountModel);
            mMyAccountPresenter.getAccountAccountInfo();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
