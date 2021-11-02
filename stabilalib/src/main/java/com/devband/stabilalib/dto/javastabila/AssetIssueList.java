package com.devband.stabilalib.dto.javastabila;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetIssueList {

    private List<AssetIssue> assetIssue;
}
