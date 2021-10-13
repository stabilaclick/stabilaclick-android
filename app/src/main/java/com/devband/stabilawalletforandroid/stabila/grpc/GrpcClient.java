package com.devband.stabilawalletforandroid.stabila.grpc;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.stabilawalletforandroid.common.Constants;
import com.google.protobuf.ByteString;

import org.stabila.api.GrpcAPI;
import org.stabila.api.GrpcAPI.AssetIssueList;
import org.stabila.api.GrpcAPI.BytesMessage;
import org.stabila.api.GrpcAPI.EmptyMessage;
import org.stabila.api.GrpcAPI.NodeList;
import org.stabila.api.GrpcAPI.NumberMessage;
import org.stabila.api.GrpcAPI.WitnessList;
import org.stabila.api.WalletGrpc;
import org.stabila.api.WalletSolidityGrpc;
import org.stabila.protos.Contract;
import org.stabila.protos.Contract.AssetIssueContract;
import org.stabila.protos.Protocol;
import org.stabila.protos.Protocol.Account;
import org.stabila.protos.Protocol.Transaction;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * https://github.com/stabilaprotocol/wallet-cli/blob/master/src/main/java/org/stabila/walletserver/GrpcClient.java
 */
public class GrpcClient {

    private ManagedChannel channel;
    private ManagedChannel channelSolidity = null;

    private WalletGrpc.WalletBlockingStub blockingStubFullNode;
    private WalletSolidityGrpc.WalletSolidityBlockingStub blockingStubSolidityNode;

    public GrpcClient(String fullNodeHost, String solidityNodeHost) {
        if (!TextUtils.isEmpty(fullNodeHost)) {
            channel = ManagedChannelBuilder.forTarget(fullNodeHost)
                    .usePlaintext()
                    .build();
            blockingStubFullNode = WalletGrpc.newBlockingStub(channel);
        }

        if (!TextUtils.isEmpty(solidityNodeHost)) {
            channelSolidity = ManagedChannelBuilder.forTarget(solidityNodeHost)
                    .usePlaintext()
                    .build();
            blockingStubSolidityNode = WalletSolidityGrpc.newBlockingStub(channelSolidity);
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Account queryAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFullNode.getAccount(request);
    }

    public GrpcAPI.TransactionExtention createTransaction(Contract.TransferContract contract) {
        return blockingStubFullNode.createTransaction2(contract);
    }

    public GrpcAPI.TransactionExtention createTransferAssetTransaction(Contract.TransferAssetContract contract) {
        return blockingStubFullNode.transferAsset2(contract);
    }

    public GrpcAPI.TransactionExtention createParticipateAssetIssueTransaction(Contract.ParticipateAssetIssueContract contract) {
        return blockingStubFullNode.participateAssetIssue2(contract);
    }

    public GrpcAPI.TransactionExtention createAssetIssue(Contract.AssetIssueContract contract) {
        return blockingStubFullNode.createAssetIssue2(contract);
    }

    public GrpcAPI.TransactionExtention voteWitnessAccount(Contract.VoteWitnessContract contract) {
        return blockingStubFullNode.voteWitnessAccount2(contract);
    }

    public GrpcAPI.TransactionExtention createWitness(Contract.WitnessCreateContract contract) {
        return blockingStubFullNode.createWitness2(contract);
    }

    public GrpcAPI.TransactionExtention createCdBalance(Contract.CdBalanceContract contract) {
        return blockingStubFullNode.cdBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createWithdrawBalance(Contract.WithdrawBalanceContract contract) {
        return blockingStubFullNode.withdrawBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createUncdBalance(Contract.UncdBalanceContract contract) {
        return blockingStubFullNode.uncdBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createUncdAsset(Contract.UncdAssetContract contract) {
        return blockingStubFullNode.uncdAsset2(contract);
    }

    public boolean broadcastTransaction(Transaction signaturedTransaction) {
        return blockingStubFullNode.broadcastTransaction(signaturedTransaction)
                .getResult();
    }

    public GrpcAPI.BlockExtention getBlock(long blockNum) {
        if (blockNum < 0) {
            return blockingStubFullNode
                    .withDeadlineAfter(Constants.GRPC_TIME_OUT_IN_MS, TimeUnit.MILLISECONDS)
                    .getNowBlock2(EmptyMessage.newBuilder().build());
        }
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        return blockingStubFullNode.getBlockByNum2(builder.build());
    }

    @Nullable
    public WitnessList listWitnesses() {
        return blockingStubFullNode.listWitnesses(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public AssetIssueList getAssetIssueList() {
        return blockingStubFullNode.getAssetIssueList(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public NodeList listNodes() {
        return blockingStubFullNode.listNodes(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public AssetIssueList getAssetIssueByAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFullNode.getAssetIssueByAccount(request);
    }

    public AssetIssueContract getAssetIssueByName(String assetName) {
        ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
        return blockingStubFullNode.getAssetIssueByName(request);
    }

    public NumberMessage getTotalTransaction() {
        return blockingStubFullNode.totalTransaction(EmptyMessage.newBuilder().build());
    }

    public GrpcAPI.ExchangeList getExchangeList() {
        return blockingStubFullNode.listExchanges(EmptyMessage.newBuilder().build());
    }

    public GrpcAPI.ExchangeList getPaginatedExchangeList(GrpcAPI.PaginatedMessage paginatedMessage) {
        return blockingStubFullNode.getPaginatedExchangeList(paginatedMessage);
    }

    public Protocol.Exchange getExchangeById(byte[] id) {
        ByteString idBS = ByteString.copyFrom(id);
        return blockingStubFullNode.getExchangeById(BytesMessage.newBuilder().setValue(idBS).build());
    }

    public GrpcAPI.TransactionExtention createExchangeCreateContract(Contract.ExchangeCreateContract contract) {
        return blockingStubFullNode.exchangeCreate(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeInjectContract(Contract.ExchangeInjectContract contract) {
        return blockingStubFullNode.exchangeInject(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeWithdrawContract(Contract.ExchangeWithdrawContract contract) {
        return blockingStubFullNode.exchangeWithdraw(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeTransactionContract(Contract.ExchangeTransactionContract contract) {
        return blockingStubFullNode.exchangeTransaction(contract);
    }

    public Protocol.SmartContract getSmartContract(byte[] address) {
        ByteString byteString = ByteString.copyFrom(address);
        BytesMessage bytesMessage = BytesMessage.newBuilder().setValue(byteString).build();
        return blockingStubFullNode.getContract(bytesMessage);
    }

    public GrpcAPI.TransactionExtention triggerContract(Contract.TriggerSmartContract triggerContract) {
        return blockingStubFullNode.triggerContract(triggerContract);
    }

    public AssetIssueContract getAssetIssueById(String id) {
        ByteString assetIdBs = ByteString.copyFrom(id.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetIdBs).build();
        if (blockingStubSolidityNode != null) {
            return blockingStubSolidityNode.getAssetIssueById(request);
        } else {
            return blockingStubFullNode.getAssetIssueById(request);
        }
    }
}
