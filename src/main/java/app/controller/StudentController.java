package app.controller;

import app.model.entity.Student;
import app.model.repository.StudentRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {
    private final StudentRepository repo;
    private final StudentResourceAssembler assembler;

    StudentController(StudentRepository repo, StudentResourceAssembler assembler) {
        this.assembler = assembler;
        this.repo = repo;
    }

    @GetMapping("/students")
    Resources<Resource<Student>> getAll() {
        List<Resource<Student>> students = repo.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(students,
                linkTo(methodOn(StudentController.class).getAll()).withSelfRel());
    }

    @PostMapping("/students")
    ResponseEntity<?> addStudent(@RequestBody Student student) throws URISyntaxException {
        Resource<Student> studentResource = assembler.toResource(repo.save(student));
        return ResponseEntity
                .created(new URI(studentResource.getId().expand().getHref()))
                .body(studentResource);
    }

    @GetMapping("/students/{id}")
    Resource<Student> getStudent(@PathVariable Integer id) {
        Student student = repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return assembler.toResource(student);
    }

    @PutMapping("/students/{id}")
    ResponseEntity<?> replaceStudent(@RequestBody Student newStudent, @PathVariable Integer id)
            throws URISyntaxException{
        Student updatedStudent = repo.findById(id)
                .map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    student.setBirthdate(newStudent.getBirthdate());
                    student.setBirthplace(newStudent.getBirthplace());
                    return repo.save(student);
                })
                .orElseGet(() -> {
                    newStudent.setStudentId(id);
                    return repo.save(newStudent);
                });

        Resource<Student> studentResource = assembler.toResource(updatedStudent);

        return ResponseEntity
                .created(new URI(studentResource.getId().expand().getHref()))
                .body(studentResource);
    }

    @DeleteMapping("students/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
