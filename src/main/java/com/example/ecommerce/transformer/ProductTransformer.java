package com.example.ecommerce.transformer;

import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.ProductRequest;
import com.example.ecommerce.dtos.response.ProductResponse;
import com.example.ecommerce.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductTransformer {

    public static Product productRequestToProduct(ProductRequest productRequest){

        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .productCategory(productRequest.getCategory())
                .productStatus(ProductStatus.AVAILABLE)
                .build();
    }


    public static ProductResponse productToProductResponse(Product product){

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getName())
                .sellerName(product.getSeller().getName())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .category(product.getProductCategory())
                .productStatus(product.getProductStatus())
                .build();
    }
}
