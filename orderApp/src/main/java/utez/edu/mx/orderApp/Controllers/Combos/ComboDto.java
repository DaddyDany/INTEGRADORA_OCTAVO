package utez.edu.mx.orderApp.Controllers.Combos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Combos.Combo;
import utez.edu.mx.orderApp.Models.Orders.OrderCombo;
import utez.edu.mx.orderApp.Models.Packages.PackageCombo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ComboDto {
    private Long comboId;
    private String comboName;
    private String comboDescription;
    private String comboImgUrl;
    private Float comboPrice;
    private Integer comboDesignatedHours;
    private Integer comboWorkersNumber;
    private List<OrderCombo> orderCombos;
    private List<PackageCombo> packageCombos;
    public Combo getCombo(){
        return new Combo(
                getComboId(),
                getComboName(),
                getComboDescription(),
                getComboImgUrl(),
                getComboPrice(),
                getComboDesignatedHours(),
                getComboWorkersNumber(),
                getOrderCombos(),
                getPackageCombos()
        );
    }

}
