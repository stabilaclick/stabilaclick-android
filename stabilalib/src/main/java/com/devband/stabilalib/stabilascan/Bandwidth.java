package com.devband.stabilalib.stabilascan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bandwidth {

    private long ucrRemaining;
    private long totalUcrLimit;
    private long totalUcrWeight;
    private long netUsed;
    private long storageLimit;
    private long storagePercentage;
    private Map<Integer, Asset> assets;
    private long netPercentage;
    private long storageUsed;
    private long storageRemaining;
    private long freeNetLimit;
    private long ucrUsed;
    private long freeNetRemaining;
    private long netLimit;
    private long netRemaining;
    private long ucrLimit;
    private long freeNetUsed;
    private long totalNetWeight;
    private long freeNetPercentage;
    private long ucrPercentage;
    private long totalNetLimit;
}
