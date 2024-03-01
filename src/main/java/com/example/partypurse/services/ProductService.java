package com.example.partypurse.services;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Product save(ProductCreationForm productDto, User user){
        Product product = new Product();
        product.setProductName(productDto.name());
        product.setPrice(productDto.price());
        product.setCategory(productDto.category());
        product.setAdder(user);
        return productRepository.save(product);
    }
    public ProductDto getInfo(Product product){
        return new ProductDto(product.getId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getAdder().getId());
    }

    public Product getById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Продукта с таким id не найдено"));
    }

    public void delete(Long id) {
        var product = getById(id);
        productRepository.delete(product);
    }
}
