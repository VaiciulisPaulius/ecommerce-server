package lt.viko.eif.pvaiciulis.service;

import lt.viko.eif.pvaiciulis.dto.request.ProductRequest;
import lt.viko.eif.pvaiciulis.dto.response.ProductResponse;
import lt.viko.eif.pvaiciulis.exception.ResourceNotFoundException;
import lt.viko.eif.pvaiciulis.model.ProductModel.Category;
import lt.viko.eif.pvaiciulis.model.ProductModel.Product;
import lt.viko.eif.pvaiciulis.model.UserModel.Address;
import lt.viko.eif.pvaiciulis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public List<ProductResponse> getAllProducts() {
        List<Product> products = repository.findAll();

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setStock(product.getStock());
            productResponse.setPrice(product.getPrice());
            productResponse.setImageUrl(product.getImageUrl());
            productResponses.add(productResponse);
        }
        return productResponses;
    }
    public ProductResponse getProduct(int id) {
        Product product = repository.findById(id).get();

        var productResponse = ProductResponse.builder()
                .name(product.getName())
                .id(product.getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().toString())
                .imageUrl(product.getImageUrl())
                .success(true)
                .build();

        return productResponse;
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
    public ProductResponse createProduct(ProductRequest request, MultipartFile image) {
        if(request.getDescription() == null || request.getName().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product name is empty")
                    .build();
        }
        if(request.getDescription() == null || request.getDescription().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product description is empty")
                    .build();
        }
        if(request.getPrice() == null || request.getPrice().toString().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Product price is empty")
                    .build();
        }
        if(request.getCategory() == null || request.getCategory().isBlank()){
            return ProductResponse.builder()
                    .success(false)
                    .error("Category is empty")
                    .build();
        }
        if(request.getStock() == null || request.getStock().toString().isBlank()){
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

        try {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get("uploads/images/" + filename);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            // Set image URL/path in product
            product.setImageUrl("/images/" + filename); // or full URL if hosted
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to store image file", e);
        }

        repository.save(product);

        return ProductResponse.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(product.getImageUrl())
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
