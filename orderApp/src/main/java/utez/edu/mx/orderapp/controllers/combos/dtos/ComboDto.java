package utez.edu.mx.orderapp.controllers.combos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.models.combos.Combo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ComboDto {
    private String comboName;
    private String comboDescription;
    private Float comboPrice;
    private Integer comboDesignatedHours;
    private Integer comboWorkersNumber;
    private List<Long> packageIds;
    private MultipartFile comboImg;
}
