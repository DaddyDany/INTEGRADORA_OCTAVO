package utez.edu.mx.orderApp.Controllers.Packages;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderApp.Controllers.Categories.CategoryDto;
import utez.edu.mx.orderApp.Models.Categories.Category;
import utez.edu.mx.orderApp.Models.Packages.Package;
import utez.edu.mx.orderApp.Services.PackageService;
import utez.edu.mx.orderApp.Utils.Response;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "http://localhost:5173")
public class PackageController {
    @Autowired
    private PackageService packageService;

    @GetMapping("/")
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.packageService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.packageService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Response<Package>> insert(
            @Valid @RequestBody PackageDto packag
    ) {
        return new ResponseEntity<>(
                this.packageService.insertPackage(packag.getPackage()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @RequestBody PackageDto packag
    ) {
        return new ResponseEntity(
                this.packageService.updatePackage(packag.getPackage()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.packageService.deletePackage(id),
                HttpStatus.OK
        );
    }
}
