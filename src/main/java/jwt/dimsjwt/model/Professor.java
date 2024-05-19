package jwt.dimsjwt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="professors")
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends User{
    private String prof_id;
    private int role = 1;
}
