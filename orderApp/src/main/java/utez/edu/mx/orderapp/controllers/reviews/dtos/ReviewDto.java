package utez.edu.mx.orderapp.controllers.reviews.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReviewDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderId;
    private String reviewDescription;
    private String score;
    private String packCombName;
}