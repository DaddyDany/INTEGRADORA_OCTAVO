package utez.edu.mx.orderApp.Models.Orders;

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
@Table(name = "order_combos")
public class OrderCombo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_combo_id")
    private Long orderComboId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;
}
