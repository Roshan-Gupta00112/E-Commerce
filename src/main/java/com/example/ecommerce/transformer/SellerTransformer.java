package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.request.SellerRequestDto;
import com.example.ecommerce.dtos.response.SellerResponseDto;
import com.example.ecommerce.model.Seller;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SellerTransformer {

    public static Seller sellerRequestDtoTOSeller(SellerRequestDto sellerRequestDto){

        return Seller.builder()
                .name(sellerRequestDto.getName())
                .emailId(sellerRequestDto.getEmailId())
                .mobNo(sellerRequestDto.getMobNo())
                .address(sellerRequestDto.getAddress())
                .build();
    }

    public static SellerResponseDto sellerToSellerResponseDto(Seller seller){
        return SellerResponseDto.builder()
                .id(seller.getId())
                .name(seller.getName())
                .emailId(seller.getEmailId())
                .mobNo(seller.getMobNo())
                .address(seller.getAddress())
                .build();
    }
}
