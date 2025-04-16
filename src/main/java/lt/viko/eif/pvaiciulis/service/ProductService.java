package lt.viko.eif.pvaiciulis.service;

import lt.viko.eif.pvaiciulis.dto.request.ProductRequest;
import lt.viko.eif.pvaiciulis.dto.response.ProductResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.ProductModel.Category;
import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing products.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    /**
     * Retrieves all products.
     */
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     */
    public Product getProductById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

//    private String name;
//    private String description;
//
//    @Column(nullable = false)
//    private Double price;
//
//    private Integer stock;
//
//    @Enumerated(EnumType.STRING)
//    private Category category;

    /**
     * Creates a new product.
     */
    public ProductResponse createProduct(ProductRequest request) {
        if(request.getName().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product name is empty")
                    .build();
        }
        if(request.getDescription().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product description is empty")
                    .build();
        }
        if(request.getPrice().toString().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product price is empty")
                    .build();
        }
        if(request.getCategory().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Category is empty")
                    .build();
        }
        if(request.getStock().toString().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product stock is empty")
                    .build();
        }

        var product = Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .category(Category.valueOf(request.getCategory()))
        .build();
        repository.save(product);

        return ProductResponse.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .success(true)
                .build();
    }

    /**
     * Updates an existing product.
     */
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(!request.getName().isBlank()){
            existingProduct.setName(request.getName());
        }
        if(!request.getDescription().isBlank()){
            existingProduct.setDescription(request.getDescription());
        }
        if(!request.getPrice().toString().isBlank()){
            existingProduct.setPrice(request.getPrice());
        }
        if(!request.getCategory().isBlank()){
            existingProduct.setCategory(Category.valueOf(request.getCategory()));
        }
        if(!request.getStock().toString().isBlank()){
            existingProduct.setStock(request.getStock());
        }
        repository.save(existingProduct);
        return ProductResponse.builder()
                .name(existingProduct.getName())
                .description(existingProduct.getDescription())
                .price(existingProduct.getPrice())
                .category(existingProduct.getCategory().toString())
                .stock(existingProduct.getStock())
                .success(true)
                .build();
    }

    /**
     * Deletes a product by its ID.
     */
    public boolean deleteProduct(Integer id) {
        // Find the product by ID
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // If product exists, delete it and return true
        repository.delete(product);
        return true;
    }

}
