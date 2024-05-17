package co.istad.idata.generated_classes.controller;

import co.istad.idata.generated_classes.domain.Student;
import co.istad.idata.generated_classes.service.StudentService;
import java.lang.Long;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
  private final StudentService service;

  @GetMapping
  public List<Student> getAllStudents() {
    return service.findAll();
  }

  @PostMapping
  public Student createStudent(@RequestBody Student entity) {
    return service.createStudent(entity);
  }

  @PutMapping
  public Student updateStudent(@RequestBody Student entity) {
    return service.updateStudent(entity);
  }

  @DeleteMapping("/{id}")
  public void deleteStudentById(@PathVariable Long id) {
    service.deleteStudentById(id);
  }
}
