package utez.edu.mx.orderapp.services.categories;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.categories.dtos.CategoryDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.repositories.categories.CategoryRepository;
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final ObjectMapper objectMapper;
    private final EncryptionService encryptionService;
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, FirebaseStorageService firebaseStorageService, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.categoryRepository = categoryRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }


    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    try{
                        return convertToCategoryDto(category);
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }

    private CategoryDto convertToCategoryDto(Category category) throws Exception{
        CategoryDto dto = new CategoryDto(category);
        String encryptedId = encryptionService.encrypt(String.valueOf(category.getServiceId()));
        dto.setServiceId(encryptedId);
        dto.setServiceName(encryptionService.encrypt(category.getServiceName()));
        dto.setServiceDescription(encryptionService.encrypt(category.getServiceDescription()));
        dto.setServiceQuote(encryptionService.encrypt(category.getServiceQuote()));
        dto.setServiceImgUrl(encryptionService.encrypt(category.getServiceImageUrl()));
        dto.setServiceState(encryptionService.encrypt(String.valueOf(category.getServiceState())));
        return dto;
    }


    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> insertCategory(String encryptedData, MultipartFile serviceImage) throws Exception{
        if (serviceImage != null && !serviceImage.isEmpty()) {
            long maxFileSize = 20 * 1024 * 1024;
            if (serviceImage.getSize() > maxFileSize) {
                throw new MaxUploadSizeExceededException(maxFileSize);
            }
        }
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        CategoryDto categoryDto = objectMapper.readValue(decryptedDataJson, CategoryDto.class);
        Category category = new Category();
        category.setServiceName(categoryDto.getServiceName());
        category.setServiceDescription(categoryDto.getServiceDescription());
        category.setServiceQuote(categoryDto.getServiceQuote());
        category.setServiceState(true);

        if (serviceImage != null && !serviceImage.isEmpty()){
            String imageUrl = firebaseStorageService.uploadFile(serviceImage, "services-pics/");
            category.setServiceImageUrl(imageUrl);
        }
        categoryRepository.save(category);
        return new Response<>("Guardado", false, 200, "El servicio ha sido agregado con exito");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateCategory(String encryptedData) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        CategoryDto categoryDto = objectMapper.readValue(decryptedDataJson, CategoryDto.class);
        Long categoryId = Long.parseLong(categoryDto.getServiceId());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
        category.setServiceName(categoryDto.getServiceName());
        category.setServiceDescription(categoryDto.getServiceDescription());
        category.setServiceQuote(categoryDto.getServiceQuote());
        categoryRepository.save(category);
        return new Response<>("Servicio actualizado", false, 200, "Información del servicio actualizada con éxito.");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> deleteCategory(String encryptedData) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        CategoryDto categoryDto = objectMapper.readValue(decryptedDataJson, CategoryDto.class);
        Long categoryId = Long.parseLong(categoryDto.getServiceId());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        if (!category.getPackages().isEmpty()) {
            return new Response<>(null, true, 400, "No se puede eliminar la categoría porque tiene paquetes asociados.");
        }

        try {
            firebaseStorageService.deleteFileFromFirebase(category.getServiceImageUrl(), "services-pics/");
        } catch (IOException e) {
            e.printStackTrace();
            return new Response<>(null, true, 500, "Error al eliminar la imagen en Firebase"
            );
        }


        categoryRepository.deleteById(category.getServiceId());
        return new Response<>("Servicio eliminado", false, 200, "Servicio eliminado con exito.");
    }
}
