package com.devband.stabilalib.stabilascan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResource {

    @JsonProperty("cded_balance_for_ucr")
    CdedBalanceForUcr cdedBalanceForUcr;
}
