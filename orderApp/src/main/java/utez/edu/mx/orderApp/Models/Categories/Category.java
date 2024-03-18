package utez.edu.mx.orderApp.Models.Categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Packages.Package;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "services")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "service_description")
    private String serviceDescription;
    @Column(name = "service_quote")
    private String serviceQuote;
    @Column(name = "service_state")
    private Boolean serviceState;
    @Column(name = "service_img_url")
    private String serviceImageUrl;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Package> packages;

    public Category(String serviceName, String serviceDescription, String serviceQuote, String serviceImageUrl) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceQuote = serviceQuote;
        this.serviceImageUrl = serviceImageUrl;
    }
}
