package utez.edu.mx.orderapp.services.packages;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.packages.dtos.ImageInfoDto;
import utez.edu.mx.orderapp.controllers.packages.dtos.PackageDto;
import utez.edu.mx.orderapp.controllers.packages.dtos.PackageInfoDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.packages.ImagePackage;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
import utez.edu.mx.orderapp.repositories.packages.ImagePackageRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PackageService {
    private final PackageRepository packageRepository;
    private final ImagePackageRepository imagePackageRepository;
    private final CategoryRepository categoryRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    public PackageService(PackageRepository packageRepository, ImagePackageRepository imagePackageRepository, FirebaseStorageService firebaseStorageService, CategoryRepository categoryRepository){
        this.packageRepository = packageRepository;
        this.imagePackageRepository = imagePackageRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Response<List<Package>> getAll() {
        return new Response<>(
                this.packageRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<Package> getOne(long id) {
        Optional<Package> aPackage = this.packageRepository.findById(id);
        return aPackage.map(value -> new Response<>(value, false, HttpStatus.OK.value(), "Package fetched successfully"))
                .orElseGet(() -> new Response<>(true, HttpStatus.NOT_FOUND.value(), "Package not found"));
    }

    public Response<PackageInfoDto> getPackageWithImages(Long packageId) {
        return packageRepository.findById(packageId).map(aPackage -> {
            List<ImageInfoDto> imageInfoDtos = aPackage.getImagePackages().stream()
                    .map(imagePackage -> new ImageInfoDto(imagePackage.getImagePackageId(), imagePackage.getImageUrl()))
                    .toList();
            PackageInfoDto packageInfoDto = new PackageInfoDto(
                    aPackage.getPackageId(),
                    aPackage.getPackageName(),
                    aPackage.getPackageDescription(),
                    aPackage.getPackagePrice(),
                    aPackage.getPackageState(),
                    aPackage.getDesignatedHours(),
                    aPackage.getWorkersNumber(),
                    aPackage.getCategory(),
                    imageInfoDtos
            );

            return new Response<>(packageInfoDto, false, HttpStatus.OK.value(), "Package fetched successfully");
        }).orElseGet(() -> new Response<>(null, true, HttpStatus.NOT_FOUND.value(), "Package not found"));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Package> insertPackage(PackageDto packageDto) throws IOException {
        if (this.packageRepository.existsByPackageName(packageDto.getPackageName())) {
            return new Response<>(null, true, 200, "Ya existe este paquete");
        }
        Category category = categoryRepository.findById(packageDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + packageDto.getCategoryId()));
        Package packag = new Package();
        packag.setPackageName(packageDto.getPackageName());
        packag.setPackageDescription(packageDto.getPackageDescription());
        packag.setPackagePrice(packageDto.getPackagePrice());
        packag.setDesignatedHours(packageDto.getDesignatedHours());
        packag.setWorkersNumber(packageDto.getWorkersNumber());
        packag.setCategory(category);
        List<ImagePackage> imagePackages = new ArrayList<>();
        for (MultipartFile file : packageDto.getImages()) {
            String imageUrl = firebaseStorageService.uploadFile(file, "package-images/");
            ImagePackage imagePackage = new ImagePackage();
            imagePackage.setImageUrl(imageUrl);
            imagePackage.setAPackage(packag);
            imagePackages.add(imagePackage);
        }
        packag = this.packageRepository.saveAndFlush(packag);
        Package finalPackage = packag;
        imagePackages.forEach(imagePackage -> imagePackage.setAPackage(finalPackage));
        imagePackageRepository.saveAll(imagePackages);

        return new Response<>(packag, false, 200, "Paquete registrado correctamente, con imágenes");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Package> updatePackage(Long id, PackageDto packageDto) {
        Category category = categoryRepository.findById(packageDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + packageDto.getCategoryId()));
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + id));
        existingPackage.setPackageName(packageDto.getPackageName());
        existingPackage.setPackageDescription(packageDto.getPackageDescription());
        existingPackage.setPackagePrice(packageDto.getPackagePrice());
        existingPackage.setDesignatedHours(packageDto.getDesignatedHours());
        existingPackage.setWorkersNumber(packageDto.getWorkersNumber());
        existingPackage.setCategory(category);
        packageRepository.save(existingPackage);
        return new Response<>(existingPackage, false, HttpStatus.OK.value(), "Paquete actualizado correctamente");
    }
    @Transactional(rollbackFor = {SQLException.class})
    public Response<Package> deletePackage(Long id) {
        Optional<Package> packageOptional = this.packageRepository.findById(id);
        if (packageOptional.isPresent()) {
            Package packag = packageOptional.get();
            for (ImagePackage imagePackage : packag.getImagePackages()) {
                try {
                    firebaseStorageService.deleteFileFromFirebase(imagePackage.getImageUrl(), "package-images/");
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Response<>(
                            null,
                            true,
                            500,
                            "Error al eliminar imágenes en Firebase"
                    );
                }
            }
            this.packageRepository.deleteById(id);
            return new Response<>(
                    null,
                    false,
                    200,
                    "Paquete eliminado correctamente"
            );
        }
        return new Response<>(
                null,
                true,
                200,
                "No existe el paquete buscado"
        );
    }
    @Transactional(rollbackFor = {SQLException.class})
    public Response<List<Package>> findAllPackagesByServiceId(Long serviceId) {
        List<Package> packages = this.packageRepository.findByServiceId(serviceId);
        if (packages.isEmpty()){
            return new Response<>(true, HttpStatus.NO_CONTENT.value(), "No packages found");
        }else{
            return new Response<>(packages, false, HttpStatus.OK.value(), "Packages fetched successfully");
        }
    }

}
