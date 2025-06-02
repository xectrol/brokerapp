package com.brokerapp.model;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    private Long id;
    private Long customerId;
    private String assetName;
    private Long size;
    private Long usableSize;
}
