package com.devband.stabilalib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StabilaAccounts {

    private long total;

    private List<StabilaAccount> data;
}
