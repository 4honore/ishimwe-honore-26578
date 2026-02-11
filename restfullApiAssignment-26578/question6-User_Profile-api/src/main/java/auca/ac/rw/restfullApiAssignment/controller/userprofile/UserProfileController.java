package auca.ac.rw.restfullApiAssignment.controller.userprofile;

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

import auca.ac.rw.restfullApiAssignment.modal.userprofile.ApiResponse;
import auca.ac.rw.restfullApiAssignment.modal.userprofile.UserProfile;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final List<UserProfile> users = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public UserProfileController() {
        users.add(new UserProfile(idGen.getAndIncrement(), "john_doe", "john@example.com", "John Doe", 28, "USA", "Software developer", true));
        users.add(new UserProfile(idGen.getAndIncrement(), "jane_smith", "jane@example.com", "Jane Smith", 32, "Canada", "Product manager", true));
        users.add(new UserProfile(idGen.getAndIncrement(), "bob_jones", "bob@example.com", "Bob Jones", 25, "UK", "Designer", true));
        users.add(new UserProfile(idGen.getAndIncrement(), "alice_wonder", "alice@example.com", "Alice Wonder", 35, "Australia", "Data scientist", false));
        users.add(new UserProfile(idGen.getAndIncrement(), "charlie_brown", "charlie@example.com", "Charlie Brown", 29, "USA", "QA Engineer", true));
    }

    @GetMapping
    public List<UserProfile> getAll() {
        return users;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getById(@PathVariable Long userId) {
        Optional<UserProfile> u = users.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
        if (u.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile retrieved successfully", u.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserProfile>> getByUsername(@PathVariable String username) {
        Optional<UserProfile> u = users.stream().filter(x -> x.getUsername().equalsIgnoreCase(username)).findFirst();
        if (u.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile retrieved successfully", u.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @GetMapping("/country/{country}")
    public List<UserProfile> getByCountry(@PathVariable String country) {
        String c = country.toLowerCase();
        return users.stream().filter(u -> u.getCountry().toLowerCase().contains(c)).collect(Collectors.toList());
    }

    @GetMapping("/age-range")
    public List<UserProfile> getByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        return users.stream().filter(u -> u.getAge() >= minAge && u.getAge() <= maxAge).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createProfile(@RequestBody UserProfile profile) {
        profile.setUserId(idGen.getAndIncrement());
        users.add(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User profile created successfully", profile));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(@PathVariable Long userId, @RequestBody UserProfile updated) {
        Optional<UserProfile> u = users.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
        if (u.isPresent()) {
            UserProfile user = u.get();
            user.setUsername(updated.getUsername());
            user.setEmail(updated.getEmail());
            user.setFullName(updated.getFullName());
            user.setAge(updated.getAge());
            user.setCountry(updated.getCountry());
            user.setBio(updated.getBio());
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile updated successfully", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateProfile(@PathVariable Long userId) {
        Optional<UserProfile> u = users.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
        if (u.isPresent()) {
            UserProfile user = u.get();
            user.setActive(true);
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile activated successfully", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateProfile(@PathVariable Long userId) {
        Optional<UserProfile> u = users.stream().filter(x -> x.getUserId().equals(userId)).findFirst();
        if (u.isPresent()) {
            UserProfile user = u.get();
            user.setActive(false);
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile deactivated successfully", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long userId) {
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        if (removed) {
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found", null));
    }

    @GetMapping("/active/list")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getActiveUsers() {
        List<UserProfile> activeUsers = users.stream().filter(UserProfile::isActive).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Active users retrieved successfully", activeUsers));
    }
}
