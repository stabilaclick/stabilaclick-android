package com.devband.stabilalib.stabilascan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private List<Object> allowExchange;
    private String address;

    @JsonProperty("cded_supply")
    private List<Object> cdedSupply;

    private Bandwidth bandwidth;
    private int accountType;
    private List<Object> exchanges;
    private Cded cded;
    private AccountResource accountResource;

    @JsonProperty("tokenBalances")
    private List<Balance> trc10TokenBalances;

    private List<Balance> balances;
    private long balance;
    private String name;
    private Delegated delegated;
    private Representative representative;

    public List<Balance> getTrc10TokenBalances() {
        List<Balance> newBalance = new ArrayList<>();

        for (Balance tokenBalance : trc10TokenBalances) {
            if ("STB".equalsIgnoreCase(tokenBalance.getName()) || "_".equalsIgnoreCase(tokenBalance.getName())) {
                continue;
            }
            newBalance.add(tokenBalance);
        }

        return newBalance;
    }
}