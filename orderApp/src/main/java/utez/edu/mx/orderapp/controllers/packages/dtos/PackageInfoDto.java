package utez.edu.mx.orderapp.controllers.packages.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.packages.Package;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PackageInfoDto {
    private String packageId;
    private String packageName;
    private String packageDescription;
    private String packagePrice;
    private String packageState;
    private String designatedHours;
    private String workersNumber;
    private String categoryName;
    private String categoryId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ImageInfoDto> images;

    public PackageInfoDto(Package aPackage) {
        this.packageName = aPackage.getPackageName();
        this.packageDescription = aPackage.getPackageDescription();
        this.categoryName = aPackage.getCategory().getServiceName();
        this.categoryId = String.valueOf(aPackage.getCategory().getServiceId());
    }
}
