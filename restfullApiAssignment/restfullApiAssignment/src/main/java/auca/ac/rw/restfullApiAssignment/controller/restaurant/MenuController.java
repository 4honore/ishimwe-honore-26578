package auca.ac.rw.restfullApiAssignment.controller.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.restfullApiAssignment.modal.restaurant.MenuItem;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final List<MenuItem> items = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public MenuController() {
        items.add(new MenuItem(idGen.getAndIncrement(), "Spring Rolls", "Crispy veggie rolls", 5.0, "Appetizer", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Caesar Salad", "Romaine, parmesan", 7.5, "Appetizer", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Grilled Salmon", "Served with veggies", 18.0, "Main Course", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Cheesecake", "Creamy NY style", 6.0, "Dessert", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Coffee", "Hot coffee", 2.5, "Beverage", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Steak", "Ribeye", 22.0, "Main Course", false));
        items.add(new MenuItem(idGen.getAndIncrement(), "Ice Cream", "Vanilla scoop", 3.5, "Dessert", true));
        items.add(new MenuItem(idGen.getAndIncrement(), "Lemonade", "Fresh lemonade", 3.0, "Beverage", true));
    }

    @GetMapping
    public List<MenuItem> getAll() {
        return items;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getById(@PathVariable Long id) {
        Optional<MenuItem> m = items.stream().filter(x -> x.getId().equals(id)).findFirst();
        return m.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{category}")
    public List<MenuItem> byCategory(@PathVariable String category) {
        String c = category.toLowerCase();
        return items.stream().filter(i -> i.getCategory().toLowerCase().contains(c)).collect(Collectors.toList());
    }

    @GetMapping("/available")
    public List<MenuItem> available(@RequestParam(required = false, defaultValue = "true") boolean available) {
        return items.stream().filter(i -> i.isAvailable() == available).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<MenuItem> searchByName(@RequestParam String name) {
        String q = name.toLowerCase();
        return items.stream().filter(i -> i.getName().toLowerCase().contains(q)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<MenuItem> add(@RequestBody MenuItem item) {
        item.setId(idGen.getAndIncrement());
        items.add(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id, @RequestBody(required = false) MenuItem payload) {
        Optional<MenuItem> m = items.stream().filter(i -> i.getId().equals(id)).findFirst();
        if (m.isPresent()) {
            MenuItem mi = m.get();
            // if payload provided, use its available value, otherwise toggle
            if (payload != null) {
                mi.setAvailable(payload.isAvailable());
            } else {
                mi.setAvailable(!mi.isAvailable());
            }
            return ResponseEntity.ok(mi);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = items.removeIf(i -> i.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
