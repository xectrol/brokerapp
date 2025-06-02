package com.brokerapp.controller.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssetResource {
    private Long id;
    private Long customerId;
    private String assetName;
    private Long size;
    private Long usableSize;
    private Long version;
}
