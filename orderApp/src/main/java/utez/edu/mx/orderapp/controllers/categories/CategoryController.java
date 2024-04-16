package utez.edu.mx.orderapp.controllers.categories;

import com.fasterxml.jackson.core.io.SegmentedStringWriter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/packages/from-service")
    public ResponseEntity<List<Package>> getAllPackagesByServiceId(@RequestBody String encryptedData) throws Exception {
        Response<List<Package>> response = this.packageService.findAllPackagesByServiceId(encryptedData);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus()));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PostMapping
    public ResponseEntity<Response<String>> insertService(@RequestPart("data") String encryptedData, @RequestParam(value = "serviceImage", required = false)MultipartFile serviceImage) throws Exception {
        Response<String> response = categoryService.insertCategory(encryptedData, serviceImage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-service")
    public ResponseEntity<Response<String>> update(@RequestPart("data") String encryptedData) throws Exception{
        Response<String> response = categoryService.updateCategory(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete-service")
    public ResponseEntity<Response<String>> delete(@RequestBody String encryptedData) throws Exception{
        Response<String> response = categoryService.deleteCategory(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
