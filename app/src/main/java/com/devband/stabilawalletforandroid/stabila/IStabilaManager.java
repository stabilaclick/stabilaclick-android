package com.devband.stabilawalletforandroid.stabila;

import org.stabila.api.GrpcAPI;
import org.stabila.api.GrpcAPI.AssetIssueList;
import org.stabila.api.GrpcAPI.NodeList;
import org.stabila.api.GrpcAPI.WitnessList;
import org.stabila.protos.Contract;
import org.stabila.protos.Protocol;
import org.stabila.protos.Protocol.Account;

import io.reactivex.Single;

public interface IStabilaManager {

    void shutdown() throws InterruptedException;

    Single<Account> queryAccount(byte[] address);

    Single<GrpcAPI.AccountResourceMessage> queryAccountResourceMessage(byte[] address);

    Single<WitnessList> listWitnesses();

    Single<AssetIssueList> getAssetIssueList();

    Single<NodeList> listNodes();

    Single<AssetIssueList> getAssetIssueByAccount(byte[] address);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.CdBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.WithdrawBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UncdBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UncdAssetContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.VoteWitnessContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferAssetContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.ParticipateAssetIssueContract contract);

    Single<Boolean> broadcastTransaction(Protocol.Transaction transaction);

    Single<GrpcAPI.BlockExtention> getBlockHeight();

    Single<GrpcAPI.ExchangeList> getExchangeList();

    Single<GrpcAPI.ExchangeList> getPaginatedExchangeList(long offset, long limit);

    Single<Protocol.Exchange> getExchangeById(String id);

    Single<GrpcAPI.TransactionExtention> createExchangeCreateContract(Contract.ExchangeCreateContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeInjectContract(Contract.ExchangeInjectContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeWithdrawContract(Contract.ExchangeWithdrawContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeTransactionContract(Contract.ExchangeTransactionContract contract);

    Single<Protocol.SmartContract> getSmartContract(byte[] address);

    Single<GrpcAPI.TransactionExtention> triggerContract(byte[] addressBytes, byte[] contractAddress, long callValue, byte[] input, long feeLimit, long tokenCallValue, String tokenId);

    Single<Contract.AssetIssueContract> getAssetIssueById(String id);
}
