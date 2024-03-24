package utez.edu.mx.orderapp.controllers.categories;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
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
    public CategoryController(CategoryService categoryService, PackageService packageService, CategoryRepository categoryRepository){
        this.categoryService = categoryService;
        this.packageService = packageService;
    }
    @GetMapping
    public ResponseEntity<Response<List<Category>>> getAll() {
        Response<List<Category>> response = this.categoryService.getAll();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Category>> getOne(@PathVariable long id) {
        Response<Category> response = this.categoryService.getOne(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/{serviceId}/packages")
    public ResponseEntity<Response<List<Package>>> getAllPackagesByServiceId(@PathVariable Long serviceId) {
        Response<List<Package>> response = this.packageService.findAllPackagesByServiceId(serviceId);
        return new ResponseEntity<>(
                response,
                HttpStatus.valueOf(response.getStatus())
        );
    }

    @PostMapping
    public ResponseEntity<Response<Category>> insert(
            @Valid @RequestBody CategoryDto service
    ) {
        Response<Category> savedService = categoryService.insertCategory(service.getCategory());
        return new ResponseEntity<>(
                savedService,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Category>> update(
            @RequestBody CategoryDto service
    ) {
        Response<Category> updatedCategory = this.categoryService.updateCategory(service.getCategory());
        return new ResponseEntity<>(
                updatedCategory,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Category>> delete(
            @PathVariable("id") Long id
    ) {
        Response<Category> deletedCategory = this.categoryService.deleteCategory(id);
        return new ResponseEntity<>(
                deletedCategory,
                HttpStatus.OK
        );
    }
}
