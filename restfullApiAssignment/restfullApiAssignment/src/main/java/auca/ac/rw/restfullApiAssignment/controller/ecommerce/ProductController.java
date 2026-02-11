package auca.ac.rw.restfullApiAssignment.controller.ecommerce;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.restfullApiAssignment.modal.ecommerce.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public ProductController() {
        products.add(new Product(idGen.getAndIncrement(), "Laptop X", "Powerful laptop", 1200.0, "Electronics", 5, "BrandA"));
        products.add(new Product(idGen.getAndIncrement(), "Phone Y", "Smartphone", 800.0, "Electronics", 10, "BrandB"));
        products.add(new Product(idGen.getAndIncrement(), "T-Shirt", "Cotton tee", 20.0, "Clothing", 50, "BrandC"));
        products.add(new Product(idGen.getAndIncrement(), "Coffee Maker", "Brew coffee", 45.0, "Home", 8, "BrandD"));
        products.add(new Product(idGen.getAndIncrement(), "Headphones", "Noise cancelling", 150.0, "Electronics", 0, "BrandA"));
        products.add(new Product(idGen.getAndIncrement(), "Sneakers", "Running shoe", 90.0, "Footwear", 25, "BrandE"));
        products.add(new Product(idGen.getAndIncrement(), "Watch", "Analog watch", 60.0, "Accessories", 12, "BrandF"));
        products.add(new Product(idGen.getAndIncrement(), "Backpack", "Travel backpack", 70.0, "Accessories", 15, "BrandG"));
        products.add(new Product(idGen.getAndIncrement(), "Blender", "Kitchen blender", 40.0, "Home", 20, "BrandH"));
        products.add(new Product(idGen.getAndIncrement(), "Desk Lamp", "LED lamp", 25.0, "Home", 30, "BrandI"));
    }

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
        if (page == null || limit == null) return products;
        int p = Math.max(0, page);
        int l = Math.max(1, limit);
        int from = p * l;
        int to = Math.min(products.size(), from + l);
        if (from >= products.size()) return List.of();
        return products.subList(from, to);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getById(@PathVariable Long productId) {
        Optional<Product> p = products.stream().filter(x -> x.getProductId().equals(productId)).findFirst();
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        String c = category.toLowerCase();
        return products.stream().filter(p -> p.getCategory().toLowerCase().contains(c)).collect(Collectors.toList());
    }

    @GetMapping("/brand/{brand}")
    public List<Product> byBrand(@PathVariable String brand) {
        String b = brand.toLowerCase();
        return products.stream().filter(p -> p.getBrand().toLowerCase().contains(b)).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        String k = keyword.toLowerCase();
        return products.stream().filter(p -> p.getName().toLowerCase().contains(k) || p.getDescription().toLowerCase().contains(k)).collect(Collectors.toList());
    }

    @GetMapping("/price-range")
    public List<Product> byPriceRange(@RequestParam Double min, @RequestParam Double max) {
        return products.stream().filter(p -> p.getPrice() >= min && p.getPrice() <= max).collect(Collectors.toList());
    }

    @GetMapping("/in-stock")
    public List<Product> inStock() {
        return products.stream().filter(p -> p.getStockQuantity() > 0).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Product> add(@RequestBody Product product) {
        product.setProductId(idGen.getAndIncrement());
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId, @RequestBody Product updated) {
        Optional<Product> p = products.stream().filter(x -> x.getProductId().equals(productId)).findFirst();
        if (p.isPresent()) {
            Product pr = p.get();
            pr.setName(updated.getName());
            pr.setDescription(updated.getDescription());
            pr.setPrice(updated.getPrice());
            pr.setCategory(updated.getCategory());
            pr.setStockQuantity(updated.getStockQuantity());
            pr.setBrand(updated.getBrand());
            return ResponseEntity.ok(pr);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> patchStock(@PathVariable Long productId, @RequestParam int quantity) {
        Optional<Product> p = products.stream().filter(x -> x.getProductId().equals(productId)).findFirst();
        if (p.isPresent()) {
            Product pr = p.get();
            pr.setStockQuantity(quantity);
            return ResponseEntity.ok(pr);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        boolean removed = products.removeIf(p -> p.getProductId().equals(productId));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
