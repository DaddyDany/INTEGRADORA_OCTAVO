package utez.edu.mx.orderapp.controllers.categories;

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
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.services.CategoryService;
import utez.edu.mx.orderapp.services.PackageService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    private final CategoryService categoryService;
    private final PackageService packageService;

    @Autowired
    public CategoryController(CategoryService categoryService, PackageService packageService){
        this.categoryService = categoryService;
        this.packageService = packageService;
    }
    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.categoryService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{serviceId}/packages")
    public ResponseEntity<List<Package>> getAllPackagesByServiceId(@PathVariable Long serviceId) {
        return new ResponseEntity(
                this.packageService.findAllPackagesByServiceId(serviceId),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.categoryService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Response<Category>> insert(
            @Valid @RequestBody CategoryDto service
    ) {
        return new ResponseEntity<>(
                this.categoryService.insertCategory(service.getCategory()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @RequestBody CategoryDto service
    ) {
        return new ResponseEntity(
                this.categoryService.updateCategory(service.getCategory()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.categoryService.deleteCategory(id),
                HttpStatus.OK
        );
    }
}
