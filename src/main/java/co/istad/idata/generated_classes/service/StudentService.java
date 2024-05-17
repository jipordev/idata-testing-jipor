package co.istad.idata.generated_classes.service;

import co.istad.idata.generated_classes.domain.Student;
import co.istad.idata.generated_classes.repository.StudentRepository;
import java.lang.Long;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
  private final StudentRepository repository;

  public List<Student> findAll() {
    return repository.findAll();
  }

  public Student createStudent(Student entity) {
    return repository.save(entity);
  }

  public Student updateStudent(Student entity) {
    return repository.save(entity);
  }

  public void deleteStudentById(Long id) {
    repository.deleteById(id);
  }

  public Student findByIdStudent(Long id) {
    return repository.findById(id).orElse(null);
  }
}
