package utez.edu.mx.orderapp.repositories.packages;

import org.springframework.data.jpa.repository.JpaRepository;
import utez.edu.mx.orderapp.models.packages.PackageCombo;

import java.util.List;

public interface PackageComboRepository extends JpaRepository<PackageCombo, Long> {
    List<PackageCombo> findByComboComboId(Long comboId);
}
