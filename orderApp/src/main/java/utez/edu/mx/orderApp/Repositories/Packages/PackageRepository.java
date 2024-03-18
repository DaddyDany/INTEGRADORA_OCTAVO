package utez.edu.mx.orderApp.Repositories.Packages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.orderApp.Models.Packages.Package;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    boolean existsByPackageName(String packageName);
    @Query("select p from Package p where p.category.serviceId = :serviceId")
    List<Package> findByServiceId(@Param("serviceId") Long serviceId);
}
