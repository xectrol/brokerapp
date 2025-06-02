package com.brokerapp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "assets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String assetName;
    private Long size;
    private Long usableSize;
}
