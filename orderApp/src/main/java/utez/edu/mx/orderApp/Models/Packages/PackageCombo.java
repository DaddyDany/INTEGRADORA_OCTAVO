package utez.edu.mx.orderApp.Models.Packages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import utez.edu.mx.orderApp.Models.Combos.Combo;

@Getter
@Setter
@Entity
@Table(name = "packages_combo")
public class PackageCombo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_combo_id")
    private Long packageComboId;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;
    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;
}
