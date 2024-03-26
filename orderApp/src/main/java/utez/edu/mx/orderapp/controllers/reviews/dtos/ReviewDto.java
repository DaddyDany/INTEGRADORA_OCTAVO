package utez.edu.mx.orderapp.controllers.reviews.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReviewDto {
    private Long orderId;
    private String review;
    private Integer score;
}