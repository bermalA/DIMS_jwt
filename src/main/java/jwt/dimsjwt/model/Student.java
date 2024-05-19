package jwt.dimsjwt.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="students")
public class Student extends User {
    private String studentId;
    private int role = 0; // Default role for students is 0 (USER)
}
