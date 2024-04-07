package utez.edu.mx.orderapp.controllers.packages.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private Long categoryId;
    private List<MultipartFile> images;
}
