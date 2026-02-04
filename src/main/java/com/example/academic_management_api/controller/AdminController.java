package com.example.academic_management_api.controller;

import com.example.academic_management_api.dto.course.CreateCourseRequest;
import com.example.academic_management_api.entity.Categories;
import com.example.academic_management_api.entity.Courses;
import com.example.academic_management_api.entity.Users;
import com.example.academic_management_api.repository.CategoryRepository;
import com.example.academic_management_api.repository.CourseRepository;
import com.example.academic_management_api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    public final UserRepository userRepository;
    public final CourseRepository courseRepository;
    public final CategoryRepository categoryRepository;

    public AdminController(UserRepository userRepository, CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        List<Users> allUsers = userRepository.findAll();
        return allUsers;
    }

    @PutMapping("/users/{id}/lock")
    public ResponseEntity<?> lockUser(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Users admin = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        if (admin.getUserId().equals(id)) {
            return ResponseEntity.badRequest()
                    .body("Bạn không thể tự khóa tài khoản của chính mình");
        }

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/unlock")
    public ResponseEntity<?> unlockUser(@PathVariable Integer id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setActive(true);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/total-users")
    public ResponseEntity<?> getTotalUsers(Authentication authentication) {
        long totalUsers = userRepository.count();

        return ResponseEntity.ok(
                Map.of("totalUsers", totalUsers)
        );
    }

    @GetMapping("/instructors")
    public List<Users> getInstructors() {
        return userRepository.findByRole("INSTRUCTOR");
    }

    @GetMapping("/courses")
    public List<Courses> getAllCourses() {
        List<Courses> allCourses = courseRepository.findAll();
        return allCourses;
    }

    @GetMapping("/total-courses")
    public ResponseEntity<?> getTotalCourses(Authentication authentication) {
        long totalCourses = courseRepository.count();

        return ResponseEntity.ok(
                Map.of("totalCourses", totalCourses)
        );
    }

    @PostMapping("/courses/add")
    public ResponseEntity<?> createCourse(
            @Valid @RequestBody CreateCourseRequest request
    ) {
        Users instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));

        Categories category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        }

        Courses course = new Courses();

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(instructor);
        course.setCategory(category);
        course.setThumbnail(request.getThumbnail());
        course.setPrice(request.getPrice());
        course.setLevel(request.getLevel());
        course.setStatus(
                request.getStatus() != null ? request.getStatus() : "draft"
        );

        Courses savedCourse = courseRepository.save(course);

        return ResponseEntity.ok(savedCourse);
    }

    @DeleteMapping("/deleted-user")
    public void deleteUser(@RequestBody Users user) {
        userRepository.deleteById(user.getUserId());
    }

    @DeleteMapping("/deleted-course")
    public void deleteCourse(@RequestBody Courses course) {
        courseRepository.deleteById(course.getCourseId());
    }
}
