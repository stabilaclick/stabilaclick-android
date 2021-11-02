package com.devband.stabilawalletforandroid.ui.main.dto;

import com.devband.stabilalib.stabilascan.Account;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StabilaAccount {

    private String name;

    private long balance;

    private long bandwidthUsed;

    private long bandwidthLimit;

    private long ucrUsed;

    private long ucrLimit;

    private long transactions;

    private long transactionIn;

    private long transactionOut;

    private List<Cded> cdedList;

    private List<Asset> assetList;

    private Account account;
}
