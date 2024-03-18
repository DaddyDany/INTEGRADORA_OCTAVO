package utez.edu.mx.orderApp.Controllers.Categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Categories.Category;

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
