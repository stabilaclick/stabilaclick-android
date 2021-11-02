package com.devband.stabilalib;

import com.devband.stabilalib.dto.AccountMedia;
import com.devband.stabilalib.dto.AccountVotes;
import com.devband.stabilalib.dto.Blocks;
import com.devband.stabilalib.dto.CoinMarketCap;
import com.devband.stabilalib.dto.Market;
import com.devband.stabilalib.dto.RichData;
import com.devband.stabilalib.dto.SystemStatus;
import com.devband.stabilalib.dto.Token;
import com.devband.stabilalib.dto.TokenHolders;
import com.devband.stabilalib.dto.Tokens;
import com.devband.stabilalib.dto.TransactionStats;
import com.devband.stabilalib.dto.Transactions;
import com.devband.stabilalib.dto.Transfers;
import com.devband.stabilalib.dto.StabilaAccounts;
import com.devband.stabilalib.dto.Votes;
import com.devband.stabilalib.dto.Witnesses;
import com.devband.stabilalib.dto.javastabila.AssetIssueList;
import com.devband.stabilalib.dto.javastabila.PaginatedRequest;
import com.devband.stabilalib.services.*;
import com.devband.stabilalib.stabilascan.Account;
import com.devband.stabilalib.stabilascan.TokenInfos;
import com.devband.stabilalib.stabilascan.Trc20Tokens;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import lombok.NonNull;

public class StabilaNetwork {

    //https://github.com/grpc-ecosystem/grpc-gateway/blob/master/runtime/errors.go#L15

    private VoteService mVoteService;
    private CoinMarketCapService mCoinMarketCapService;
    private StabilaScanService mStabilaScanService;
    private TokenService mTokenService;
    private AccountService mAccountService;
    private JavaStabilaHttpService mJavaStabilaHttpService;

    public StabilaNetwork(VoteService voteService, CoinMarketCapService coinMarketCapService,
                          StabilaScanService stabilaScanService, TokenService tokenService,
                          AccountService accountService, JavaStabilaHttpService javaStabilaHttpService) {
        this.mVoteService = voteService;
        this.mCoinMarketCapService = coinMarketCapService;
        this.mStabilaScanService = stabilaScanService;
        this.mTokenService = tokenService;
        this.mAccountService = accountService;
        this.mJavaStabilaHttpService = javaStabilaHttpService;
    }

    public Single<Witnesses> getVoteWitnesses() {
        return mVoteService.getVoteWitnesses();
    }

    public Single<Votes> getVoteCurrentCycle() {
        return mVoteService.getVoteCurrentCycle();
    }

    public Single<Map<String, Long>> getRemainNextCycle() {
        return mVoteService.getRemainNextCycle();
    }

    public Single<List<CoinMarketCap>> getCoinInfo(String symbol) {
        return mCoinMarketCapService.getPrice(symbol);
    }

    public Single<Transfers> getTransfers(String sort, boolean hasCount, int limit,
            long start, long block) {
        return mStabilaScanService.getTransfers(sort, hasCount, limit, start, block);
    }

    public Single<Transfers> getTransfers(String sort, boolean hasCount, int limit,
            long start, String tokenName) {
        return mStabilaScanService.getTransfers(sort, hasCount, limit, start, tokenName);
    }

    public Single<Transfers> getTransfersByAddress(String sort, boolean hasTotal, int limit, long start, String address) {
        return mStabilaScanService.getTransfersByAddress(sort, hasTotal, limit, start, address);
    }

    public Single<Transfers> getTransfers(String address, String symbol) {
        return mStabilaScanService.getTransfers(address, symbol);
    }

    public Single<Transfers> getTransfers(String address, long start, int limit, String sort, boolean hasTotal) {
        return mStabilaScanService.getTransfers(address, start, limit, sort, hasTotal);
    }

    public Single<Transfers> getTransfers(long start, int limit, String sort, boolean hasTotal) {
        return mStabilaScanService.getTransfers(start, limit, sort, hasTotal);
    }

    public Single<Transfers> getTransfers(int start, int limit, String sort, boolean hasTotal, String address) {
        return mStabilaScanService.getTransfers(start, limit, sort, hasTotal, address);
    }

    public Single<Tokens> getTokens(long start, int limit, String sort, String status) {
        return mTokenService.getTokens(start, limit, sort, status);
    }

    public Single<Token> getTokenDetail(String tokenName) {
        return mTokenService.getTokenDetail(tokenName);
    }

    public Single<TokenHolders> getTokenHolders(String tokenName, long start, int limit, String sort) {
        return mTokenService.getTokenHolders(tokenName, start, limit, sort);
    }

    public Single<Tokens> findTokens(String query, long start, int limit, String sort) {
        return mTokenService.findTokens(query, start, limit, sort);
    }

    public Single<List<Market>> getMarkets() {
        return mStabilaScanService.getMarket();
    }

    public Single<Blocks> getBlocks(int limit, long start) {
        return mStabilaScanService.getBlocks("-number", limit, start);
    }

    public Single<Blocks> getBlock(long blockNumber) {
        return mStabilaScanService.getBlock("-number", 1, blockNumber);
    }

    public Single<RichData> getRichList() {
        return mAccountService.getRichData();
    }

    public Single<Account> getAccountInfo(String address) {
        return mAccountService.getAccountInfo(address);
    }

    public Single<StabilaAccounts> getAccounts(long start, int limit, String sort) {
        return mAccountService.getAccounts(start, limit, sort);
    }

    public Single<TransactionStats> getTransactionStats(String address) {
        return mStabilaScanService.getTransactionStats(address);
    }

    public Single<AccountVotes> getAccountVotes(String voterAddress, long start, int limit, String sort) {
        return mVoteService.getAccountVotes(voterAddress, start, limit, sort);
    }

    public Single<Transactions> getTransactions(long start, int limit, String sort, boolean hasTotal) {
        return mStabilaScanService.getTransactions(start, limit, sort, hasTotal);
    }

    public Single<Transactions> getTransactions(String address, long start, int limit, String sort, boolean hasTotal) {
        return mStabilaScanService.getTransactions(address, start, limit, sort, hasTotal);
    }

    public Single<Transactions> getTransactions(long block, long start, int limit, String sort, boolean hasTotal) {
        return mStabilaScanService.getTransactions(sort, hasTotal, limit, start, block);
    }

    public Single<AccountMedia> getAccountMedia(String address) {
        return mAccountService.getAccountMedia(address);
    }

    public Single<SystemStatus> getSystemStatus() {
        return mStabilaScanService.getStatus();
    }

    public Single<TokenInfos> getTokenInfo(String id) {
        return mStabilaScanService.getTokenInfo(id, 1);
    }

    public Single<Trc20Tokens> getTrc20TokenList() {
        return mTokenService.getTrc20Tokens("", 0, "issue_time");
    }

    public Single<Trc20Tokens> getTrc20Token(@NonNull String contractAddress) {
        return mTokenService.getTrc20Tokens(contractAddress, 0, "issue_time");
    }

    public Single<AssetIssueList> getPaginatedAssetIssueList(int start, int limit) {
        return mJavaStabilaHttpService.getPaginatedAssetIssueList(new PaginatedRequest(start, limit));
    }
}
