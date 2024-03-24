package utez.edu.mx.orderapp.repositories.combos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.combos.Combo;
@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
    boolean existsByComboName(String comboName);
}
