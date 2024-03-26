package utez.edu.mx.orderapp.controllers.packages.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ImageInfoDto {
    private Long imagePackageId;
    private String imageUrl;
    public ImageInfoDto(Long imagePackageId, String imageUrl) {
        this.imagePackageId = imagePackageId;
        this.imageUrl = imageUrl;
    }
}
