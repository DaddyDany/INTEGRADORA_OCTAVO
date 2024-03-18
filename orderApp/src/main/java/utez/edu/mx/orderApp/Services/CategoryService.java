package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Models.Categories.Category;
import utez.edu.mx.orderApp.Repositories.Categories.CategoryRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.sql.SQLException;
import java.util.List;


@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Response getAll() {
        return new Response<List<Category>>(
                this.categoryRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response getOne(long id) {
        return new Response<Object>(
                this.categoryRepository.findById(id),
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response insertCategory(Category service) {
        if (this.categoryRepository.existsByServiceName(service.getServiceName()))
            return new Response(
                    null,
                    true,
                    200,
                    "Ya existe este servicio"
            );
        service.setServiceState(false);
        return new Response(
                this.categoryRepository.saveAndFlush(service),
                false,
                200,
                "Servicio registrado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response updateCategory(Category servicio) {
        if (this.categoryRepository.existsById(servicio.getServiceId()))
            return new Response(
                    this.categoryRepository.saveAndFlush(servicio),
                    false,
                    200,
                    "Servicio actualizado correctamente"
            );
        return new Response(
                null,
                true,
                200,
                "No existe el servicio buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response deleteCategory(Long id) {
        if (this.categoryRepository.existsById(id)) {
            this.categoryRepository.deleteById(id);
            return new Response(
                    null,
                    false,
                    200,
                    "Servicio eliminado correctamente"
            );
        }
        return new Response(
                null,
                true,
                200,
                "No existe el servicio buscado"
        );
    }
}
