package utez.edu.mx.orderApp.Models;

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
    @Column(name = "user_token")
    private String userToken;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "userAttribute")
    private List<Administrator> administrators;
    @OneToMany(mappedBy = "userAttribute")
    private List<CommonUser> commonUsers;
    @OneToMany(mappedBy = "userAttribute")
    private List<Worker> workers;
}
