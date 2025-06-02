package com.brokerapp.mapper;


import com.brokerapp.controller.dto.DepositDto;
import com.brokerapp.controller.dto.WithdrawDto;
import com.brokerapp.controller.resource.AssetResource;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;
import com.brokerapp.persistence.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetMapper INSTANCE = Mappers.getMapper( AssetMapper.class );

    AssetResource assetToAssetResource(Asset asset);

    AssetEntity assetToAssetEntity(Asset asset);

    Asset assetEntityToAsset(AssetEntity assetEntity);

    Deposit depositCommandToDeposit(DepositDto depositDto);
    Withdraw withdrawCommandToDeposit(WithdrawDto withdrawDto);
}
