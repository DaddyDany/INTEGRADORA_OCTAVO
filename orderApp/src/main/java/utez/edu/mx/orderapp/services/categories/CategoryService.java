package utez.edu.mx.orderapp.services.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
import utez.edu.mx.orderapp.utils.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
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
    public Response<Category> insertCategory(Category service) {
        if (this.categoryRepository.existsByServiceName(service.getServiceName()))
            return new Response<>(
                    null,
                    true,
                    200,
                    "Ya existe este servicio"
            );
        service.setServiceState(false);
        return new Response<>(
                this.categoryRepository.saveAndFlush(service),
                false,
                200,
                "Servicio registrado correctamente"
        );
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
