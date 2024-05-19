package jwt.dimsjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @Column(length = 16)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(length = 16)
    private String name;
    @Column(nullable = false)
    private int role; // 1 for ADMIN, 0 for USER
    @Column(name="is_locked", columnDefinition = "boolean default false")
    private boolean isLocked;
}