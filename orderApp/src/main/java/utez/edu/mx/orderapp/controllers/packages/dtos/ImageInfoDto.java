package utez.edu.mx.orderapp.controllers.packages.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ImageInfoDto {
    private String imageUrl;
    public ImageInfoDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
