package mate.academy.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", unique = true, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public enum RoleName {
        ROLE_USER, ROLE_ADMIN
    }
}
