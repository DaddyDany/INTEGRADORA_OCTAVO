package utez.edu.mx.orderapp.services.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FirebaseStorageService firebaseStorageService;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, FirebaseStorageService firebaseStorageService){
        this.categoryRepository = categoryRepository;
        this.firebaseStorageService = firebaseStorageService;
    }


    @Transactional(readOnly = true)
    public Response<List<Category>> getAll() {
        List<Category> categories = this.categoryRepository.findAll();
        if (categories.isEmpty()) {
            return new Response<>(true, HttpStatus.NO_CONTENT.value(), "No categories found");
        } else {
            return new Response<>(categories, false, HttpStatus.OK.value(), "Categories fetched successfully");
        }
    }

    @Transactional(readOnly = true)
    public Response<Category> getOne(long id) {
        Optional<Category> category = this.categoryRepository.findById(id);
        return category.map(value -> new Response<>(value, false, HttpStatus.OK.value(), "Category fetched successfully"))
                .orElseGet(() -> new Response<>(true, HttpStatus.NOT_FOUND.value(), "Category not found"));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Long> insertCategory(CategoryDto categoryDto) {
       try{
           Category category = new Category();
           category.setServiceName(categoryDto.getServiceName());
           category.setServiceDescription(categoryDto.getServiceDescription());
           category.setServiceQuote(categoryDto.getServiceQuote());
           if (categoryDto.getServiceImage() != null && !categoryDto.getServiceImage().isEmpty()){
               String imageUrl = firebaseStorageService.uploadFile(categoryDto.getServiceImage(), "services-pics/");
               category.setServiceImageUrl(imageUrl);
           }
           category = categoryRepository.save(category);
           return new Response<>(category.getServiceId(), false, 200, "El servicio ha sido agregado con exito");
       }catch (RuntimeException e) {
           return new Response<>(true, 200, "Hubo un error registrando el servicio: " + e.getMessage());
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Category> updateCategory(Category service) {
        if (this.categoryRepository.existsById(service.getServiceId()))
            return new Response<>(
                    this.categoryRepository.saveAndFlush(service),
                    false,
                    200,
                    "Servicio actualizado correctamente"
            );
        return new Response<>(
                null,
                true,
                200,
                "No existe el servicio buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Category> deleteCategory(Long id) {
        if (this.categoryRepository.existsById(id)) {
            this.categoryRepository.deleteById(id);
            return new Response<>(
                    null,
                    false,
                    200,
                    "Servicio eliminado correctamente"
            );
        }
        return new Response<>(
                null,
                true,
                200,
                "No existe el servicio buscado"
        );
    }
}
