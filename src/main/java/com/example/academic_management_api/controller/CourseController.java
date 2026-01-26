package com.example.academic_management_api.controller;

import com.example.academic_management_api.dto.CourseResponseDto;
import com.example.academic_management_api.entity.Categories;
import com.example.academic_management_api.entity.Courses;
import com.example.academic_management_api.entity.Users;
import com.example.academic_management_api.repository.CategoryRepository;
import com.example.academic_management_api.repository.CourseRepository;
import com.example.academic_management_api.repository.UserRepository;
import io.jsonwebtoken.lang.Classes;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<Courses> getAllCourses() {
        List<Courses> allCourses = courseRepository.findAll();
        return allCourses;
    }

    @GetMapping("/allDetail")
    public List<CourseResponseDto> findAllClassDetail() {
        List<Object[]> results = courseRepository.findAllCoursesDetail();
        List<CourseResponseDto> details = new ArrayList<>();

        for (Object[] row: results) {
            System.out.println(row[0]);
            //1 -> Object[] [-1, "Java Core", 2, "Dao", "F6", 2025-12-12]
            CourseResponseDto courseDetail = new CourseResponseDto(
                    (Integer) row[0],    // courseId
                    (String) row[1],     // title
                    (String) row[2],    // description
                    (BigDecimal) row[3], // price
                    (String) row[4],     // thumbnail
                    (String) row[5],     // instructorUsername
                    (String) row[6],     // instructorFullName
                    (Integer) row[7],    // categoryId
                    (String) row[8]     // categoryName
            );
            details.add(courseDetail);
        }
        return details;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseDetail(@PathVariable Integer id) {
        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // instructor
        Users instructor = userRepository
                .findByUsername(course.getInstructorUsername())
                .orElseThrow(null);

        // category
        Categories category = categoryRepository
                .findById(course.getCategoryId())
                .orElseThrow(null);

        String thumbnail =
                course.getThumbnail() != null && !course.getThumbnail().isEmpty()
                        ? course.getThumbnail()
                        : CourseResponseDto.DEFAULT_THUMBNAIL;

        CourseResponseDto response = new CourseResponseDto(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getThumbnail(),
                instructor.getUsername(),
                instructor.getFullName(),
                category.getCategoryId(),
                category.getCategoryName()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/course-detail")
    public Courses getClassDetail(@RequestParam Integer classId) {
        return courseRepository.findById(classId).orElse(null);
    }
}
