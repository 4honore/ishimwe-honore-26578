package auca.ac.rw.restfullApiAssignment.controller.library;
import auca.ac.rw.restfullApiAssignment.modal.Book;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private  final List<Book> books = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public BookController() {
        books.add(new Book(idGen.getAndIncrement(), "clean code", "Robert Martin", "978-0132350884", 2008));
        books.add(new Book(idGen.getAndIncrement(), "Effective java", "joshua bloch", "978-0134685991", 2018));
        books.add(new Book(idGen.getAndIncrement(), "Design patterns", "Erich Gamma", "978-0201633610", 1994));
    }

    @GetMapping
    public List<Book> getAll() {
        return books;
    }
@GetMapping("/{id}")
public ResponseEntity<Book> getById(@PathVariable Long id) {
    Optional<Book> b = books.stream().filter(x ->x.getId() == id).findFirst();
    return b.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
}
@GetMapping("/search")
    public List<Book> searchBytitle(@RequestParam String title) {
        String q = title.toLowerCase();
        return books.stream().filter(x -> x.getTitle().toLowerCase().contains(q)).collect(Collectors.toList());
    }
       
@PostMapping
public ResponseEntity<Book> addBook(@RequestBody Book book) {
    book.setId(idGen.getAndIncrement());
    books.add(book);
    return ResponseEntity.status(HttpStatus.CREATED).body(book);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    boolean removed = books.removeIf(x -> x.getId() == id);
   return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}
}

