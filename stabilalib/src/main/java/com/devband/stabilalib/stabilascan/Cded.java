package com.devband.stabilalib.stabilascan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cded {

    private long total;
    private List<CdedStb> balances;
}
