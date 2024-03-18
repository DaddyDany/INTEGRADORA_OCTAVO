package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Models.Packages.Package;
import utez.edu.mx.orderApp.Repositories.Packages.PackageRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
public class PackageService {
    @Autowired
    private PackageRepository packageRepository;

    @Transactional(readOnly = true)
    public Response getAll() {
        return new Response<List<Package>>(
                this.packageRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response getOne(long id) {
        return new Response<Object>(
                this.packageRepository.findById(id),
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response insertPackage(Package packag) {
        if (this.packageRepository.existsByPackageName(packag.getPackageName()))
            return new Response(
                    null,
                    true,
                    200,
                    "Ya existe este paquete"
            );
        return new Response(
                this.packageRepository.saveAndFlush(packag),
                false,
                200,
                "Paquete registrado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response updatePackage(Package packag) {
        if (this.packageRepository.existsById(packag.getPackageId()))
            return new Response(
                    this.packageRepository.saveAndFlush(packag),
                    false,
                    200,
                    "Paquete actualizado correctamente"
            );
        return new Response(
                null,
                true,
                200,
                "No existe el paquete buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response deletePackage(Long id) {
        if (this.packageRepository.existsById(id)) {
            this.packageRepository.deleteById(id);
            return new Response(
                    null,
                    false,
                    200,
                    "Paquete eliminado correctamente"
            );
        }
        return new Response(
                null,
                true,
                200,
                "No existe el paquete buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response findAllPackagesByServiceId(Long serviceId) {
        return new Response<List<Package>>(
                this.packageRepository.findByServiceId(serviceId),
                false,
                200,
                "OK"
        );
    }

}
