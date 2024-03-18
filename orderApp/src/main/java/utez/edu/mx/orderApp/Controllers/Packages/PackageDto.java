package utez.edu.mx.orderApp.Controllers.Packages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Categories.Category;
import utez.edu.mx.orderApp.Models.Orders.OrderPackage;
import utez.edu.mx.orderApp.Models.Packages.Package;
import utez.edu.mx.orderApp.Models.Packages.PackageCombo;

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

    @Override
    public String toString() {
        return "PackageDto{" +
                "packageName='" + packageName + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", packagePrice=" + packagePrice +
                ", designatedHours=" + designatedHours +
                ", workersNumber=" + workersNumber +
                ", category=" + category +
                '}';
    }
}
