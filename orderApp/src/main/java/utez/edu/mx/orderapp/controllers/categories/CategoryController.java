package utez.edu.mx.orderapp.controllers.categories;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
import utez.edu.mx.orderapp.services.categories.CategoryService;
import utez.edu.mx.orderapp.services.packages.PackageService;
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
    public ResponseEntity<List<Category>> getAll() {
        Response<List<Category>> response = this.categoryService.getAll();
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus()));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Long id) {
        Response<Category> response = this.categoryService.getOne(id);
        if(response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus()));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{serviceId}/packages")
    public ResponseEntity<List<Package>> getAllPackagesByServiceId(@PathVariable Long serviceId) {
        Response<List<Package>> response = this.packageService.findAllPackagesByServiceId(serviceId);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus()));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PostMapping
    public ResponseEntity<Response<Long>> insertService(@Valid @ModelAttribute CategoryDto service) {
        Response<Long> response = categoryService.insertCategory(service);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") Long id, @RequestBody CategoryDto serviceDto){
        Response<Category> response = this.categoryService.updateCategory(id, serviceDto);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable("id") Long id) {
        Response<Category> response = this.categoryService.deleteCategory(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }
}
