package com.example.partypurse.services;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.request.ProductUpdateForm;
import com.example.partypurse.dto.response.ProductInfoDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Продукта с таким id не найдено"));
    }

    public List<ProductInfoDto> getAll(List<Product> products) {
        return products.stream().map(this::getInfo).toList();
    }


    public Product save(ProductCreationForm productDto) {
        Product product = new Product();
        product.setProductName(productDto.name());
        product.setPrice(productDto.price());
        product.setCategory(productDto.category());
        product.setAdder(null);
        return productRepository.save(product);
    }

    public void save(Product product) {
        productRepository.save(product);
    }


    // Проверить обновляется ли без сохранения в комнате
    public ProductInfoDto update(Long productId, ProductUpdateForm productPayload) {

        var product = getById(productId);

        if (productPayload.name() != null)
            product.setProductName(productPayload.name());
        if (productPayload.category() != null)
            product.setCategory(productPayload.category());
        if (productPayload.price() != null)
            product.setPrice(productPayload.price());

        return getInfo(productRepository.save(product));
    }

    public ProductInfoDto getInfo(Product product) {
        return new ProductInfoDto(product.getId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getAdder().getId());
    }


    public void delete(Long id) {
        var product = getById(id);
        productRepository.delete(product);
    }


}
