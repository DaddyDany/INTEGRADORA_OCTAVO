package utez.edu.mx.orderapp.repositories.packages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderapp.models.packages.Package;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    boolean existsByPackageName(String packageName);
    @Query("select p from Package p where p.category.serviceId = :serviceId")
    List<Package> findByServiceId(@Param("serviceId") Long serviceId);
}
