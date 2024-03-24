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
import utez.edu.mx.orderapp.models.orders.Order;
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
    public ResponseEntity<Response<List<Package>>> getAll() {
        return new ResponseEntity<>(
                this.packageService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Package>> getOne(
            @PathVariable("id") Long id
    ) {
        Response<Package> packageResponse = this.packageService.getOne(id);
        return new ResponseEntity<>(
                packageResponse,
                HttpStatus.OK
        );
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Package>> insert(
            @Valid @ModelAttribute PackageDto packag
    ) throws IOException {
        return new ResponseEntity<>(
                this.packageService.insertPackage(packag),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Package>> update(
            @RequestBody PackageDto packag
    ) {
        return new ResponseEntity<>(
                this.packageService.updatePackage(packag.getPackage()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Package>> delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                this.packageService.deletePackage(id),
                HttpStatus.OK
        );
    }
}
