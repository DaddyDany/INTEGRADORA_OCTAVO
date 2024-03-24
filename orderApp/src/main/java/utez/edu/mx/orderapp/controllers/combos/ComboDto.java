package utez.edu.mx.orderapp.controllers.combos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderapp.models.combos.Combo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ComboDto {
    private String comboName;
    private String comboDescription;
    private String comboImgUrl;
    private Float comboPrice;
    private Integer comboDesignatedHours;
    private Integer comboWorkersNumber;
    private List<Long> packageIds;

    public Combo getCombo(){
        return new Combo(
                getComboName(),
                getComboDescription(),
                getComboImgUrl(),
                getComboPrice(),
                getComboDesignatedHours(),
                getComboWorkersNumber()
        );
    }
}
