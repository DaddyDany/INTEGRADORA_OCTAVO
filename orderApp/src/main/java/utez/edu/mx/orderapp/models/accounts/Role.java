package utez.edu.mx.orderapp.models.accounts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_description")
    private String roleDescription;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<Administrator> administrators;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<Worker> workers;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<CommonUser> commonUsers;
}
