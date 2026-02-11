package auca.ac.rw.restfullApiAssignment.controller.studentRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.restfullApiAssignment.modal.studentRegistration.Student;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public StudentController() {
        students.add(new Student(idGen.getAndIncrement(), "Alice", "A", "alice@example.com", "Computer Science", 3.8));
        students.add(new Student(idGen.getAndIncrement(), "Bob", "B", "bob@example.com", "Mathematics", 3.2));
        students.add(new Student(idGen.getAndIncrement(), "Charlie", "C", "charlie@example.com", "Computer Science", 3.6));
        students.add(new Student(idGen.getAndIncrement(), "Diana", "D", "diana@example.com", "Biology", 3.9));
        students.add(new Student(idGen.getAndIncrement(), "Evan", "E", "evan@example.com", "Engineering", 2.9));
    }

    @GetMapping
    public List<Student> getAll() {
        return students;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getById(@PathVariable Long studentId) {
        Optional<Student> s = students.stream().filter(x -> x.getStudentId().equals(studentId)).findFirst();
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/major/{major}")
    public List<Student> getByMajor(@PathVariable String major) {
        String m = major.toLowerCase();
        return students.stream().filter(s -> s.getMajor().toLowerCase().contains(m)).collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<Student> filterByGpa(@RequestParam Double gpa) {
        return students.stream().filter(s -> s.getGpa() >= gpa).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Student> register(@RequestBody Student student) {
        student.setStudentId(idGen.getAndIncrement());
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> update(@PathVariable Long studentId, @RequestBody Student updated) {
        Optional<Student> s = students.stream().filter(x -> x.getStudentId().equals(studentId)).findFirst();
        if (s.isPresent()) {
            Student st = s.get();
            st.setFirstName(updated.getFirstName());
            st.setLastName(updated.getLastName());
            st.setEmail(updated.getEmail());
            st.setMajor(updated.getMajor());
            st.setGpa(updated.getGpa());
            return ResponseEntity.ok(st);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
