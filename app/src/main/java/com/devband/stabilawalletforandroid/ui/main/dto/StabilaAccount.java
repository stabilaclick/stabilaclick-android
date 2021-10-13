package com.devband.stabilawalletforandroid.ui.main.dto;

import com.devband.stabilalib.stabilascan.Account;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StabilaAccount {

    private String name;

    private long balance;

    private long bandwidth;

    private long transactions;

    private long transactionIn;

    private long transactionOut;

    private List<Cded> cdedList;

    private List<Asset> assetList;

    private Account account;
}
