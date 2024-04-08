package utez.edu.mx.orderapp.controllers.packages;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import utez.edu.mx.orderapp.services.packages.PackageService;
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "http://localhost:5173")
public class PackageController {
    private final PackageService packageService;
    private final EncryptionService encryptionService;

    private final ObjectMapper objectMapper;

    @Autowired
    public PackageController(PackageService packageService, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.packageService = packageService;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Package>> getAll() {
        Response<List<Package>> response = this.packageService.getAll();
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getOne(@PathVariable("id") Long id) {
        Response<Package> response = this.packageService.getOne(id);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/package-info-users/{id}")
    public ResponseEntity<String> getPackage(@PathVariable("id") Long id) {
        Response<PackageInfoDto> response = packageService.getPackageWithImages(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getData().getPackageName());
        } else {
            return ResponseEntity
                    .status(HttpStatus.valueOf(response.getStatus()))
                    .body(response.getMessage());
        }
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
