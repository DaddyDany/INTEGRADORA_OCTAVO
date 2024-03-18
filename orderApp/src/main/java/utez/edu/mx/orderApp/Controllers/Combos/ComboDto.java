package utez.edu.mx.orderApp.Controllers.Combos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Combos.Combo;

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
    @Override
    public String toString() {
        return "ComboDto{" +
                "comboName='" + comboName + '\'' +
                ", comboDescription='" + comboDescription + '\'' +
                ", comboImgUrl='" + comboImgUrl + '\'' +
                ", comboPrice=" + comboPrice +
                ", comboDesignatedHours=" + comboDesignatedHours +
                ", comboWorkersNumber=" + comboWorkersNumber +
                ", packageIds=" + packageIds +
                '}';
    }
}
