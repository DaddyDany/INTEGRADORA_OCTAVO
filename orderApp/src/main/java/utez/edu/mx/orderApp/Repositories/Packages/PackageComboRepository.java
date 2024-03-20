package utez.edu.mx.orderApp.Repositories.Packages;

import org.springframework.data.jpa.repository.JpaRepository;
import utez.edu.mx.orderApp.Models.Packages.PackageCombo;

import java.util.List;

public interface PackageComboRepository extends JpaRepository<PackageCombo, Long> {
    List<PackageCombo> findByComboComboId(Long comboId);
}
