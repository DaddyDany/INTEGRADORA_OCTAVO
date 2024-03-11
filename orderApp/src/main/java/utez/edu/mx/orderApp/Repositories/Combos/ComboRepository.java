package utez.edu.mx.orderApp.Repositories.Combos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Combos.Combo;
@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
    boolean existsByComboName(String comboName);
}
