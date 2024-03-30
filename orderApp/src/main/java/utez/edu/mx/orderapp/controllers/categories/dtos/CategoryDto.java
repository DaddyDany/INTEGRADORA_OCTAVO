package utez.edu.mx.orderapp.controllers.categories.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String serviceName;
    private String serviceDescription;
    private String serviceQuote;
    private MultipartFile serviceImage;
    private String serviceImgUrl;

    @JsonProperty("serviceImageUrl")
    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImgUrl = serviceImageUrl;
    }
    public Category getCategory(){
        return new Category(
                getServiceName(),
                getServiceDescription(),
                getServiceQuote(),
                getServiceImgUrl()
        );
    }
}
