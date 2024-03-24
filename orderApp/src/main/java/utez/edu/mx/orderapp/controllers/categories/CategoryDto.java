package utez.edu.mx.orderapp.controllers.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.categories.Category;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDto {
    private String serviceName;
    private String serviceDescription;
    private String serviceQuote;
    private String serviceImageUrl;

    public Category getCategory(){
        return new Category(
                getServiceName(),
                getServiceDescription(),
                getServiceQuote(),
                getServiceImageUrl()
        );
    }
}
