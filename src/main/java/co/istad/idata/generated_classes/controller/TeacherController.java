package co.istad.idata.generated_classes.controller;

import co.istad.idata.generated_classes.domain.Teacher;
import co.istad.idata.generated_classes.service.TeacherService;
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
@RequestMapping("/api/v1/teachers")
public class TeacherController {
  private final TeacherService service;

  @GetMapping
  public List<Teacher> getAllTeachers() {
    return service.findAll();
  }

  @PostMapping
  public Teacher createTeacher(@RequestBody Teacher entity) {
    return service.createTeacher(entity);
  }

  @PutMapping
  public Teacher updateTeacher(@RequestBody Teacher entity) {
    return service.updateTeacher(entity);
  }

  @DeleteMapping("/{id}")
  public void deleteTeacherById(@PathVariable Long id) {
    service.deleteTeacherById(id);
  }
}
