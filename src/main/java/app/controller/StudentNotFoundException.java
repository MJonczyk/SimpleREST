package app.controller;

public class StudentNotFoundException extends RuntimeException {
    StudentNotFoundException(Integer id) {
        super("Could not find Student " + id);
    }
}
