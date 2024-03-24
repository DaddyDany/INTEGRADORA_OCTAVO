package utez.edu.mx.orderapp.controllers.packages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.packages.Package;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PackageDto {
    private String packageName;
    private String packageDescription;
    private Float packagePrice;
    private Integer designatedHours;
    private Integer workersNumber;
    private Category category;
    private List<MultipartFile> images;
    public Package getPackage(){
        return new Package(
                getPackageName(),
                getPackageDescription(),
                getPackagePrice(),
                getDesignatedHours(),
                getWorkersNumber(),
                getCategory()
        );
    }
}
