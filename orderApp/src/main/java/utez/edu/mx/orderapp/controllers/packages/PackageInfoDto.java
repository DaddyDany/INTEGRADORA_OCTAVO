package utez.edu.mx.orderapp.controllers.packages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.categories.Category;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PackageInfoDto {
    private Long packageId;
    private String packageName;
    private String packageDescription;
    private Float packagePrice;
    private Boolean packageState;
    private Integer designatedHours;
    private Integer workersNumber;
    private Category category;
    private List<ImageInfoDto> images;
}
