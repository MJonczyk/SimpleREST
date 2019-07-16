package app.controller;

import app.model.entity.Student;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class StudentResourceAssembler implements ResourceAssembler<Student, Resource<Student>> {

    @Override
    public Resource<Student> toResource(Student student) {
        return new Resource<>(student,
                linkTo(methodOn(StudentController.class).getStudent(student.getStudentId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).getAll()).withRel("students"));
    }
 }
