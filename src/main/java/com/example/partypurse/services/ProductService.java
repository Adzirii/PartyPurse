package com.example.partypurse.services;

import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.models.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public ProductDto getInfo(Product product){
        return new ProductDto(product.getId(), product.getProductName(), product.getCategory(), product.getPrice());
    }
}
