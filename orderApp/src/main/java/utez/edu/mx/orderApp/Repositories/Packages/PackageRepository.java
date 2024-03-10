package utez.edu.mx.orderApp.Repositories.Packages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Packages.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    boolean existsByPackageName(String packageName);
}
