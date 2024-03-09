package utez.edu.mx.orderApp.Models.Accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users_attributes")
public class UserAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_attributes_id")
    private Long userAttributesId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_first_last_name")
    private String userFirstLastName;
    @Column(name = "user_second_last_name")
    private String userSecondLastName;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_cellphone")
    private String userCellphone;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "userAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Administrator> administrators;
    @JsonIgnore
    @OneToMany(mappedBy = "userAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommonUser> commonUsers;
    @JsonIgnore
    @OneToMany(mappedBy = "userAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Worker> workers;
}
