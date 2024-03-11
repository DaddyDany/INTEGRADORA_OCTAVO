package utez.edu.mx.orderApp.Models.Combos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Orders.OrderCombo;
import utez.edu.mx.orderApp.Models.Packages.PackageCombo;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "combos")
@NoArgsConstructor
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "combo_id")
    private Long comboId;
    @Column(name = "combo_name")
    private String comboName;
    @Column(name = "combo_description")
    private String comboDescription;
    @Column(name = "combo_img_url")
    private String comboImgUrl;
    @Column(name = "combo_price")
    private Float comboPrice;
    @Column(name = "combo_designated_hours")
    private Integer comboDesignatedHours;
    @Column(name = "combo_workers_number")
    private Integer comboWorkersNumber;
    @JsonIgnore
    @OneToMany(mappedBy = "combo")
    private List<OrderCombo> orderCombos;
    @JsonIgnore
    @OneToMany(mappedBy = "combo")
    private List<PackageCombo> packageCombos;
    public Combo(Long comboId, String comboName, String comboDescription, String comboImgUrl, Float comboPrice, Integer comboDesignatedHours, Integer comboWorkersNumber, List<OrderCombo> orderCombos, List<PackageCombo> packageCombos) {
        this.comboId = comboId;
        this.comboName = comboName;
        this.comboDescription = comboDescription;
        this.comboImgUrl = comboImgUrl;
        this.comboPrice = comboPrice;
        this.comboDesignatedHours = comboDesignatedHours;
        this.comboWorkersNumber = comboWorkersNumber;
        this.orderCombos = orderCombos;
        this.packageCombos = packageCombos;
    }
}
