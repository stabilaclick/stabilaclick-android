package com.devband.stabilalib.dto.javastabila;

import com.devband.stabilalib.utils.ByteStringToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetIssue {
    private String id;

    private String ownerAddress;

    @JsonDeserialize(converter = ByteStringToStringConverter.class)
    private String name;

    @JsonDeserialize(converter = ByteStringToStringConverter.class)
    private String abbr;

    private long totalSupply;

    List<CdedSupply> cdedSupply;

    private int stbNum;

    private int precision;

    private int num;

    private long startTime;

    private long endTime;

    private long order; // useless

    private int voteScore;

    @JsonDeserialize(converter = ByteStringToStringConverter.class)
    private String description;

    @JsonDeserialize(converter = ByteStringToStringConverter.class)
    private String url;

    private long freeAssetNetLimit;

    private long publicFreeAssetNetLimit;

    private long publicFreeAssetNetUsage;

    private long publicLatestFreeNetTime;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class CdedSupply {
        private long cdedAmount;

        private long cdedDays;
    }
}
