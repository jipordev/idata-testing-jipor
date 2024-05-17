package co.istad.idata.generated_classes.service;

import co.istad.idata.generated_classes.domain.Teacher;
import co.istad.idata.generated_classes.repository.TeacherRepository;
import java.lang.Long;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
  private final TeacherRepository repository;

  public List<Teacher> findAll() {
    return repository.findAll();
  }

  public Teacher createTeacher(Teacher entity) {
    return repository.save(entity);
  }

  public Teacher updateTeacher(Teacher entity) {
    return repository.save(entity);
  }

  public void deleteTeacherById(Long id) {
    repository.deleteById(id);
  }

  public Teacher findByIdTeacher(Long id) {
    return repository.findById(id).orElse(null);
  }
}
