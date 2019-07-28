package app.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer studentId;
    private String universityIndex;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String birthplace;
}
