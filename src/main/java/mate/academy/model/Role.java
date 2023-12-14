package mate.academy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", unique = true, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
