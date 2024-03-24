package utez.edu.mx.orderapp.controllers.packages;

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
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.services.PackageService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "http://localhost:5173")
public class PackageController {
    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService){
        this.packageService = packageService;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Package> insert(@Valid @ModelAttribute PackageDto packag) throws IOException {
        Response<Package> response = this.packageService.insertPackage(packag);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Package> update(@PathVariable("id") Long id, @RequestBody PackageDto packageDto) {
        Package packag = packageDto.getPackage();
        packag.setPackageId(id);
        Response<Package> response = this.packageService.updatePackage(packag);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Package> delete(@PathVariable("id") Long id) {
        Response<Package> response = this.packageService.deletePackage(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }
}
