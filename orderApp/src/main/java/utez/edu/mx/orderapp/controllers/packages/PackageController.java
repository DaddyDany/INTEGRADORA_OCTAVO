package utez.edu.mx.orderapp.controllers.packages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.packages.dtos.PackageDto;
import utez.edu.mx.orderapp.controllers.packages.dtos.PackageInfoDto;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.services.packages.PackageService;
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "http://localhost:5173")
public class PackageController {
    private final PackageService packageService;
    private final EncryptionService encryptionService;
private final PackageRepository packageRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PackageController(PackageService packageService, EncryptionService encryptionService, PackageRepository packageRepository, ObjectMapper objectMapper){
        this.packageService = packageService;
        this.encryptionService = encryptionService;
        this.packageRepository = packageRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<PackageInfoDto> getAll() {
        return packageRepository.findAll().stream()
                .map(pack -> {
                    try{
                        return convertToPackageInfoDto(pack);
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }

    private PackageInfoDto convertToPackageInfoDto(Package aPackage) throws Exception{
        PackageInfoDto dto = new PackageInfoDto(aPackage);
        String encryptedId = encryptionService.encrypt(String.valueOf(aPackage.getPackageId()));
        dto.setPackageId(encryptedId);
        dto.setPackageName(encryptionService.encrypt(aPackage.getPackageName()));
        dto.setPackageDescription(encryptionService.encrypt(aPackage.getPackageDescription()));
        String encryptedPrice = encryptionService.encrypt(String.valueOf(aPackage.getPackagePrice()));
        dto.setPackagePrice(encryptedPrice);
        String encryptedState = encryptionService.encrypt(String.valueOf(aPackage.getPackageState()));
        dto.setPackageState(encryptedState);
        String encryptedHours = encryptionService.encrypt(String.valueOf(aPackage.getDesignatedHours()));
        dto.setDesignatedHours(encryptedHours);
        String encryptedWorkersNumber = encryptionService.encrypt(String.valueOf(aPackage.getWorkersNumber()));
        dto.setWorkersNumber(encryptedWorkersNumber);
        dto.setCategoryName(encryptionService.encrypt(aPackage.getCategory().getServiceName()));
        String encryptedCategoryId = encryptionService.encrypt(String.valueOf(aPackage.getCategory().getServiceId()));
        dto.setCategoryId(encryptionService.encrypt(encryptedCategoryId));
        return dto;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Package> getOne(@PathVariable("id") Long id) {
//        Response<Package> response = this.packageService.getOne(id);
//        if (response.isSuccess()) {
//            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
//        }
//    }

    @PostMapping("/package-info-users")
    public ResponseEntity<PackageInfoDto> getPackage(@RequestBody String encryptedData) throws Exception {
        Response<PackageInfoDto> response = packageService.getPackageWithImages(encryptedData);
        return ResponseEntity.ok(response.getData());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Package>> insertPackage(
            @RequestPart("data") String encryptedData,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        PackageDto packageDto = objectMapper.readValue(decryptedDataJson, PackageDto.class);
        Response<Package> response = packageService.insertPackage(packageDto, images);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PutMapping("/update-package")
    public ResponseEntity<Response<String>> update(@RequestPart("data") String encryptedData) throws Exception{
        Response<String> response = packageService.updatePackage(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete-package")
    public ResponseEntity<Response<String>> delete(@RequestBody String encryptedData) throws Exception{
        Response<String> response = packageService.deletePackage(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
