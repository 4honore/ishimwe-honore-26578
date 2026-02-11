package auca.ac.rw.restfullApiAssignment.controller.library;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.restfullApiAssignment.modal.library.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public BookController() {
        books.add(new Book(idGen.getAndIncrement(), "Clean Code", "Robert Martin", "978-0132350884", 2008));
        books.add(new Book(idGen.getAndIncrement(), "Effective Java", "Joshua Bloch", "978-0134685991", 2018));
        books.add(new Book(idGen.getAndIncrement(), "Refactoring", "Martin Fowler", "978-0201485677", 1999));
    }

    @GetMapping
    public List<Book> getAll() {
        return books;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        Optional<Book> b = books.stream().filter(x -> x.getId().equals(id)).findFirst();
        return b.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search")
    public List<Book> searchByTitle(@RequestParam String title) {
        String q = title.toLowerCase();
        return books.stream().filter(b -> b.getTitle().toLowerCase().contains(q)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        book.setId(idGen.getAndIncrement());
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
