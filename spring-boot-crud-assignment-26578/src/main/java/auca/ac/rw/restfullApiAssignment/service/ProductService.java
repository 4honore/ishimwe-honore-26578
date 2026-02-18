package auca.ac.rw.restfullApiAssignment.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auca.ac.rw.restfullApiAssignment.modal.Product;
import auca.ac.rw.restfullApiAssignment.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public String saveProduct(Product product) {
        repository.save(product);
        return "Product saved successfully.";
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public String updateProduct(Long id, Product newProductData) {
        return repository.findById(id).map(product -> {
            product.setName(newProductData.getName());
            product.setPrice(newProductData.getPrice());
            // Add other fields from your Product model here
            repository.save(product);
            return "Product updated successfully.";
        }).orElse("Product not found.");
    }

    public String deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Product deleted successfully.";
        }
        return "Product not found.";
    }
}
