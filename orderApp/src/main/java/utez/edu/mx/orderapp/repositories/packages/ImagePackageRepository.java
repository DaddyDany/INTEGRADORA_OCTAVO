package utez.edu.mx.orderapp.repositories.packages;

import org.springframework.data.jpa.repository.JpaRepository;
import utez.edu.mx.orderapp.models.packages.ImagePackage;

public interface ImagePackageRepository extends JpaRepository<ImagePackage, Long> {
}
