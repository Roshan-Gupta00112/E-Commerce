package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.request.SellerRequest;
import com.example.ecommerce.dtos.response.SellerResponseDto;
import com.example.ecommerce.model.Seller;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SellerTransformer {

    public static Seller sellerRequestDtoTOSeller(SellerRequest sellerRequest){

        return Seller.builder()
                .name(sellerRequest.getName())
                .emailId(sellerRequest.getEmailId())
                .mobNo(sellerRequest.getMobNo())
                .address(sellerRequest.getAddress())
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
