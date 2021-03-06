package com.devband.stabilawalletforandroid.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.devband.stabilalib.StabilaNetwork;
import com.devband.stabilalib.dto.CoinMarketCap;
import com.devband.stabilawalletforandroid.R;
import com.devband.stabilawalletforandroid.common.CommonActivity;
import com.devband.stabilawalletforandroid.common.Constants;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends CommonActivity {

    @Inject
    StabilaNetwork mStabilaNetwork;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_layout)
    public LinearLayout mViewLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_stabila);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_about_stabila);
        }

        mStabilaNetwork.getCoinInfo(Constants.STABILA_COINMARKET_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps != null && !coinMarketCaps.isEmpty()) {
                            CoinMarketCap coinMarketCap = coinMarketCaps.get(0);

                            Date updated = new Date(Long.parseLong(coinMarketCap.getLastUpdated()) * 1_000);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Market Price : ")
                                    .append(Constants.usdFormat.format(Double.parseDouble(coinMarketCap.getPriceUsd())))
                                    .append(" USD (")
                                    .append("-".equals(coinMarketCap.getPercentChange24h().substring(0, 1)) ?
                                            coinMarketCap.getPercentChange24h() :
                                            "+" + coinMarketCap.getPercentChange24h()
                                    ).append("%)");

                            Element whitePaper = new Element()
                                    .setTitle(getString(R.string.white_paper_text))
                                    .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new FinestWebView.Builder(AboutActivity.this).show(getString(R.string.stabila_white_paper_url));
                                            }
                                        });

                            Element architecture = new Element()
                                    .setTitle(getString(R.string.stabila_architecture_text))
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new FinestWebView.Builder(AboutActivity.this).show(getString(R.string.stabila_architecture_url));
                                        }
                                    });

                            View aboutPage = new AboutPage(AboutActivity.this)
                                    .isRTL(false)
                                    .setDescription("STABILA is an ambitious project dedicated to building the infrastructure for a truly decentralized Internet. The Stabila Protocol, one of the largest blockchain based operating systems in the world, offers scalable, high-availability and high-throughput support that underlies all the decentralized applications in the STABILA ecosystem.\n" +
                                            "\n" +
                                            "STABILA Protocol and the SVM allow anyone to develop DAPPs for themselves or their communities, with smart contracts making decentralized crowdfunding and token issuance easier than ever. Stabila DAPP projects already include Peiwo, Obike, Uplive, game.com, Kitty live and Mico, with 100M+ active users from more than 100 countries and regions around the world.")
                                    .addGroup("Stabila info (" + Constants.sdf.format(updated) + ")")
                                    .addItem(new Element().setTitle("Name : STABILA"))
                                    .addItem(new Element().setTitle("Symbol : STB"))
                                    .addItem(new Element().setTitle("Total Supply : " + Constants.numberFormat.format(Double.parseDouble(coinMarketCap.getTotalSupply())) + " STB"))
                                    .addItem(new Element().setTitle("Supply : " + Constants.numberFormat.format(Double.parseDouble(coinMarketCap.getAvailableSupply())) + " STB"))
                                    .addItem(new Element().setTitle(sb.toString()))
                                    .addItem(new Element().setTitle("Market Rank : " + coinMarketCap.getRank()))
                                    .addItem(new Element().setTitle("Market Cap : " + Constants.usdFormat.format(Double.parseDouble(coinMarketCap.getMarketCapUsd())) + " USD"))
                                    .addItem(new Element().setTitle("24H Volume : " + Constants.usdFormat.format(Double.parseDouble(coinMarketCap.get_24hVolumeUsd())) + " USD"))
                                    //.addItem(whitePaper)
                                    //.addItem(architecture)
                                    .addGroup("Connect with stabila team")
                                    .addWebsite("https://stabila.network")
                                    .addFacebook("stabilafoundation")
                                    .addTwitter("Stabilafoundation")
                                    .addGitHub("stabilaprotocol")
                                    .create();

                            mViewLayout.addView(aboutPage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
