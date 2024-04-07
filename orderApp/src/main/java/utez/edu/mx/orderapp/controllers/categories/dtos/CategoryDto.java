package utez.edu.mx.orderapp.controllers.categories.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.models.categories.Category;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDto {
    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceQuote;
    private MultipartFile serviceImage;
    private String serviceImgUrl;

    public CategoryDto(Category category){
        this.serviceName = category.getServiceName();
        this.serviceDescription = category.getServiceDescription();
        this.serviceQuote = category.getServiceQuote();
        this.serviceImgUrl = category.getServiceImageUrl();
    }

}
