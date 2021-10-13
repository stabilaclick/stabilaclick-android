package com.devband.stabilalib.stabilascan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CdedStb {

    private long expires;
    private long amount;
}
