package utez.edu.mx.orderApp.Controllers.Categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Categories.Category;
import utez.edu.mx.orderApp.Models.Packages.Package;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDto {
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceQuote;
    private Boolean serviceState;
    private String serviceImageUrl;
    private List<Package> packages;
    public Category getCategory(){
        return new Category(
                getServiceId(),
                getServiceName(),
                getServiceDescription(),
                getServiceQuote(),
                getServiceState(),
                getServiceImageUrl(),
                getPackages()
        );
    }
}