package com.example.ecommerce.transformer;

import com.example.ecommerce.Enum.ProductCategory;
import com.example.ecommerce.Enum.ProductStatus;
import com.example.ecommerce.dtos.request.ProductRequest;
import com.example.ecommerce.dtos.response.ProductResponse;
import com.example.ecommerce.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductTransformer {

    public static Product productRequestToProduct(ProductRequest productRequest){

        Product product= Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .totalQuantityAdded(productRequest.getQuantity())
                .productCategory(productRequest.getCategory())
                .productStatus(ProductStatus.AVAILABLE)
                .build();
        // Setting maxOrderedQuantity & warrantyPeriods attributes of Product
        if(productRequest.getCategory()== ProductCategory.MOBILE_LAPTOP){
            product.setMaxOrderedQuantity("2");
            product.setWarrantyPeriods("12 Months");
        }
        else if(productRequest.getCategory()==ProductCategory.FOOD) product.setMaxOrderedQuantity("20");
        else if(productRequest.getCategory()==ProductCategory.SPORTS) product.setMaxOrderedQuantity("10");
        else if(productRequest.getCategory()==ProductCategory.BEAUTY) {
            product.setMaxOrderedQuantity("15");
            product.setWarrantyPeriods("24 Months");
        }
        else if(productRequest.getCategory()==ProductCategory.FASHION) product.setMaxOrderedQuantity("15");
        else if(productRequest.getCategory()==ProductCategory.CLOTH) product.setMaxOrderedQuantity("15");
        else if(productRequest.getCategory()==ProductCategory.ELECTRONICS){
            product.setMaxOrderedQuantity("2");
            product.setWarrantyPeriods("12 Months");
        }
        else if(productRequest.getCategory()==ProductCategory.HOME) product.setMaxOrderedQuantity("20");
        else if(productRequest.getCategory()==ProductCategory.GROCERY) product.setMaxOrderedQuantity("25");

        return product;
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
