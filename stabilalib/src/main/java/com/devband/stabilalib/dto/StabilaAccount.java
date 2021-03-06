package com.devband.stabilalib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StabilaAccount {

    private String address;

    private long totalSupply;

    private long availableSypply;

    private long balance;

    private double balancePercent;

    private long power;
}
