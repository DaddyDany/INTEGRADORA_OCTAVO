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
    private Long packageId;
    private String packageName;
    private String packageDescription;
    private Float packagePrice;
    private Boolean packageState;
    private Integer designatedHours;
    private Integer workersNumber;
    private Category category;
    private List<OrderPackage> orderPackages;
    private List<PackageCombo> packageCombos;
    public Package getPackage(){
        return new Package(
                getPackageId(),
                getPackageName(),
                getPackageDescription(),
                getPackagePrice(),
                getPackageState(),
                getDesignatedHours(),
                getWorkersNumber(),
                getCategory(),
                getOrderPackages(),
                getPackageCombos()
        );
    }

}
