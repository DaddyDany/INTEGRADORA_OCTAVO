package utez.edu.mx.orderapp.services.packages;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import utez.edu.mx.orderapp.utils.EncryptionService;
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
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PackageService(PackageRepository packageRepository, ImagePackageRepository imagePackageRepository, FirebaseStorageService firebaseStorageService, CategoryRepository categoryRepository, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.packageRepository = packageRepository;
        this.imagePackageRepository = imagePackageRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.categoryRepository = categoryRepository;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
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
                    .map(imagePackage -> new ImageInfoDto(imagePackage.getImageUrl()))
                    .toList();
            PackageInfoDto packageInfoDto = new PackageInfoDto(
                    String.valueOf(aPackage.getPackageId()),
                    aPackage.getPackageName(),
                    aPackage.getPackageDescription(),
                    String.valueOf(aPackage.getPackagePrice()),
                    String.valueOf(aPackage.getPackageState()),
                    String.valueOf(aPackage.getDesignatedHours()),
                    String.valueOf(aPackage.getWorkersNumber()),
                    aPackage.getCategory().getServiceName(),
                    String.valueOf(aPackage.getCategory().getServiceId()),
                    imageInfoDtos
            );
            return new Response<>(packageInfoDto, false, HttpStatus.OK.value(), "Package fetched successfully");
        }).orElseGet(() -> new Response<>(null, true, HttpStatus.NOT_FOUND.value(), "Package not found"));
    }

    @Transactional(rollbackFor = {Exception.class})
    public Response<Package> insertPackage(PackageDto packageDto, MultipartFile[] images) {
        if (packageRepository.existsByPackageName(packageDto.getPackageName())) {
            return new Response<>(null, true, 409, "Ya existe un paquete con este nombre.");
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
        packag = packageRepository.saveAndFlush(packag);
        if (images != null) {
            List<ImagePackage> imagePackages = new ArrayList<>();
            for (MultipartFile file : images) {
                try {
                    String imageUrl = firebaseStorageService.uploadFile(file, "package-images/");
                    ImagePackage imagePackage = new ImagePackage();
                    imagePackage.setImageUrl(imageUrl);
                    imagePackage.setAPackage(packag);
                    imagePackages.add(imagePackage);
                } catch (IOException e) {
                    throw new RuntimeException("Error al subir la imagen", e);
                }
            }
            if (!imagePackages.isEmpty()) {
                imagePackageRepository.saveAll(imagePackages);
            }
        }

        return new Response<>(packag, false, 200, "Paquete registrado correctamente.");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updatePackage(String encryptedData) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        PackageDto packageDto = objectMapper.readValue(decryptedDataJson, PackageDto.class);
        Long packageId = Long.parseLong(packageDto.getPackageId());
        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + packageId));
        Category category = categoryRepository.findById(packageDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + packageDto.getCategoryId()));
        existingPackage.setPackageName(packageDto.getPackageName());
        existingPackage.setPackageDescription(packageDto.getPackageDescription());
        existingPackage.setPackagePrice(packageDto.getPackagePrice());
        existingPackage.setDesignatedHours(packageDto.getDesignatedHours());
        existingPackage.setWorkersNumber(packageDto.getWorkersNumber());
        existingPackage.setCategory(category);
        packageRepository.save(existingPackage);

        return new Response<>("Paquete actualizado", false, HttpStatus.OK.value(), "Paquete actualizado correctamente");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> deletePackage(String encryptedData) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        PackageDto packageDto = objectMapper.readValue(decryptedDataJson, PackageDto.class);
        Long packageId = Long.parseLong(packageDto.getPackageId());
        Package aPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado"));

        if (!aPackage.getOrderPackages().isEmpty() && !aPackage.getPackageCombos().isEmpty()) {
            return new Response<>(null, true, 400, "No se puede eliminar el paquete porque pertenece a algun combo o tiene ordenes registradas.");
        }

        for (ImagePackage imagePackage : aPackage.getImagePackages()) {
            try {
                firebaseStorageService.deleteFileFromFirebase(imagePackage.getImageUrl(), "package-images/");
            } catch (IOException e) {
                e.printStackTrace();
                return new Response<>(null, true, 500, "Error al eliminar imágenes en Firebase"
                );
            }
        }
        packageRepository.deleteById(aPackage.getPackageId());
        return new Response<>("Paquete eliminado", false, 200, "Paquete eliminado correctamente"
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
