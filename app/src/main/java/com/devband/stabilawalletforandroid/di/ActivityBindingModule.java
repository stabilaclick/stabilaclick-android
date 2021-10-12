package com.devband.stabilawalletforandroid.di;

import com.devband.stabilawalletforandroid.ui.blockexplorer.account.AccountModule;
import com.devband.stabilawalletforandroid.ui.blockexplorer.block.BlockModule;
import com.devband.stabilawalletforandroid.ui.blockexplorer.overview.OverviewModule;
import com.devband.stabilawalletforandroid.ui.blockexplorer.transaction.TransactionModule;
import com.devband.stabilawalletforandroid.ui.blockexplorer.transfer.TransferModule;
import com.devband.stabilawalletforandroid.ui.token.holder.HolderModule;
import com.devband.stabilawalletforandroid.ui.about.AboutActivity;
import com.devband.stabilawalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.stabilawalletforandroid.ui.accountdetail.overview.AccountVoteActivity;
import com.devband.stabilawalletforandroid.ui.accountdetail.overview.AccountVoteActivityModule;
import com.devband.stabilawalletforandroid.ui.accountdetail.representative.RepresentativeModule;
import com.devband.stabilawalletforandroid.ui.address.AddressActivity;
import com.devband.stabilawalletforandroid.ui.address.AddressActivityModule;
import com.devband.stabilawalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.stabilawalletforandroid.ui.backupaccount.BackupAccountActivityModule;
import com.devband.stabilawalletforandroid.ui.blockdetail.BlockDetailActivity;
import com.devband.stabilawalletforandroid.ui.blockdetail.fragment.BlockInfoModule;
import com.devband.stabilawalletforandroid.ui.blockexplorer.BlockExplorerActivity;
import com.devband.stabilawalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.stabilawalletforandroid.ui.createwallet.CreateWalletActivityModule;
import com.devband.stabilawalletforandroid.ui.exchange.ExchangeActivity;
import com.devband.stabilawalletforandroid.ui.exchange.ExchangeActivityModule;
import com.devband.stabilawalletforandroid.ui.importkey.ImportPrivateKeyActivity;
import com.devband.stabilawalletforandroid.ui.importkey.ImportPrivateKeyActivityModule;
import com.devband.stabilawalletforandroid.ui.intro.IntroActivity;
import com.devband.stabilawalletforandroid.ui.intro.IntroActivityModule;
import com.devband.stabilawalletforandroid.ui.login.LoginActivity;
import com.devband.stabilawalletforandroid.ui.login.LoginActivityModule;
import com.devband.stabilawalletforandroid.ui.main.MainActivity;
import com.devband.stabilawalletforandroid.ui.main.MainActivityModule;
import com.devband.stabilawalletforandroid.ui.market.MarketActivity;
import com.devband.stabilawalletforandroid.ui.market.MarketActivityModule;
import com.devband.stabilawalletforandroid.ui.more.MoreActivity;
import com.devband.stabilawalletforandroid.ui.more.MoreActivityModule;
import com.devband.stabilawalletforandroid.ui.myaccount.MyAccountActivity;
import com.devband.stabilawalletforandroid.ui.myaccount.MyAccountActivityModule;
import com.devband.stabilawalletforandroid.ui.mytransfer.MyTransferActivityModule;
import com.devband.stabilawalletforandroid.ui.mytransfer.TransferActivity;
import com.devband.stabilawalletforandroid.ui.node.NodeActivity;
import com.devband.stabilawalletforandroid.ui.node.NodeActivityModule;
import com.devband.stabilawalletforandroid.ui.previewwallet.PreviewWalletActivity;
import com.devband.stabilawalletforandroid.ui.previewwallet.PreviewWalletActivityModule;
import com.devband.stabilawalletforandroid.ui.qrscan.QrScanActivity;
import com.devband.stabilawalletforandroid.ui.representative.RepresentativeActivity;
import com.devband.stabilawalletforandroid.ui.representative.RepresentativeActivityModule;
import com.devband.stabilawalletforandroid.ui.requestcoin.RequestCoinActivity;
import com.devband.stabilawalletforandroid.ui.sendtrc10.SendTrc10Activity;
import com.devband.stabilawalletforandroid.ui.sendtrc10.SendTrc10ActivityModule;
import com.devband.stabilawalletforandroid.ui.sendtrc20.SendTrc20Activity;
import com.devband.stabilawalletforandroid.ui.sendtrc20.SendTrc20ActivityModule;
import com.devband.stabilawalletforandroid.ui.smartcontract.TestSmartContractActivity;
import com.devband.stabilawalletforandroid.ui.token.TokenActivity;
import com.devband.stabilawalletforandroid.ui.token.TokenActivityModule;
import com.devband.stabilawalletforandroid.ui.token.TokenDetailActivity;
import com.devband.stabilawalletforandroid.ui.vote.VoteActivity;
import com.devband.stabilawalletforandroid.ui.vote.VoteActivityModule;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = { IntroActivityModule.class })
    abstract IntroActivity bindIntroActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { LoginActivityModule.class })
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AboutActivity bindAboutActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract TestSmartContractActivity bindTestSmartContractActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { AddressActivityModule.class })
    abstract AddressActivity bindAddressActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { BackupAccountActivityModule.class })
    abstract BackupAccountActivity bindBackupAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { CreateWalletActivityModule.class })
    abstract CreateWalletActivity bindCreateWalletActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { ImportPrivateKeyActivityModule.class })
    abstract ImportPrivateKeyActivity bindImportPrivateKeyActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MainActivityModule.class })
    abstract MainActivity bindMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MarketActivityModule.class })
    abstract MarketActivity bindMarketActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MoreActivityModule.class })
    abstract MoreActivity bindMoreActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MyAccountActivityModule.class })
    abstract MyAccountActivity bindMyAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MyTransferActivityModule.class })
    abstract TransferActivity bindTransferActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { NodeActivityModule.class })
    abstract NodeActivity bindNodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { RepresentativeActivityModule.class })
    abstract RepresentativeActivity bindRepresentativeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { SendTrc10ActivityModule.class })
    abstract SendTrc10Activity bindSendTokenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { SendTrc20ActivityModule.class })
    abstract SendTrc20Activity bindSendTrc20Activity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { TokenActivityModule.class })
    abstract TokenActivity bindTokenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { VoteActivityModule.class })
    abstract VoteActivity bindVoteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            com.devband.stabilawalletforandroid.ui.accountdetail.overview.OverviewModule.class,
            com.devband.stabilawalletforandroid.ui.accountdetail.transaction.TransactionModule.class,
            com.devband.stabilawalletforandroid.ui.accountdetail.transfer.TransferModule.class,
            RepresentativeModule.class
    })
    abstract AccountDetailActivity bindAccountDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccountVoteActivityModule.class})
    abstract AccountVoteActivity bindAccountVoteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            OverviewModule.class,
            AccountModule.class,
            BlockModule.class,
            TransactionModule.class,
            TransferModule.class
    })
    abstract BlockExplorerActivity bindBlockExplorerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            BlockInfoModule.class,
            com.devband.stabilawalletforandroid.ui.accountdetail.transaction.TransactionModule.class,
            com.devband.stabilawalletforandroid.ui.accountdetail.transfer.TransferModule.class
    })
    abstract BlockDetailActivity bindBlockDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract QrScanActivity bindQrScanActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RequestCoinActivity bindRequestCoinActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            com.devband.stabilawalletforandroid.ui.token.overview.OverviewModule.class,
            HolderModule.class,
            com.devband.stabilawalletforandroid.ui.token.transfer.TransferModule.class
    })
    abstract TokenDetailActivity bindTokenDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { ExchangeActivityModule.class })
    abstract ExchangeActivity exchangeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { PreviewWalletActivityModule.class })
    abstract PreviewWalletActivity bindPreviewWalletActivity();
}
