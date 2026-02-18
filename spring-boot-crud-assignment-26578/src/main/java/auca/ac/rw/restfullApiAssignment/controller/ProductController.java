package auca.ac.rw.restfullApiAssignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import auca.ac.rw.restfullApiAssignment.modal.Product;
import auca.ac.rw.restfullApiAssignment.service.ProductService;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController { 

    @Autowired
    private ProductService productserve;

    @PostMapping(value = "/addProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        String response = productserve.saveProduct(product);
        return new ResponseEntity<>(response, response.contains("successfully") ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productserve.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Product product = productserve.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        String response = productserve.updateProduct(id, product);
        return new ResponseEntity<>(response, response.contains("successfully") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String response = productserve.deleteProduct(id);
        return new ResponseEntity<>(response, response.contains("successfully") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
