package utez.edu.mx.orderapp.services.combos;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.orderapp.controllers.combos.dtos.ComboDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.models.packages.PackageCombo;
import utez.edu.mx.orderapp.repositories.combos.ComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComboService {
    private final ComboRepository comboRepository;
    private final PackageComboRepository packageComboRepository;
    private final PackageRepository packageRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;
    @Autowired
    public ComboService(ComboRepository comboRepository, PackageComboRepository packageComboRepository, PackageRepository packageRepository, FirebaseStorageService firebaseStorageService, EncryptionService encryptionService, ObjectMapper objectMapper){
        this.comboRepository = comboRepository;
        this.packageComboRepository = packageComboRepository;
        this.packageRepository = packageRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Response<List<Combo>> getAll() {
        return new Response<>(
                this.comboRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response<Combo> getOne(long id) {
        Optional<Combo> combo = this.comboRepository.findById(id);
        return combo.map(value -> new Response<>(value, false, HttpStatus.OK.value(), "Combo fetched successfully"))
                .orElseGet(() -> new Response<>(true, HttpStatus.NOT_FOUND.value(), "Combo not found"));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> insertCombo(String encryptedData, MultipartFile comboImg) throws Exception{
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        ComboDto comboDto = objectMapper.readValue(decryptedDataJson, ComboDto.class);
        Combo combo = new Combo();
        combo.setComboName(comboDto.getComboName());
        combo.setComboDescription(comboDto.getComboDescription());
        combo.setComboPrice(comboDto.getComboPrice());
        combo.setComboDesignatedHours(comboDto.getComboDesignatedHours());
        combo.setComboWorkersNumber(comboDto.getComboWorkersNumber());
        if (comboImg != null && !comboImg.isEmpty()){
            String imageUrl = firebaseStorageService.uploadFile(comboImg, "combos-pics/");
            combo.setComboImgUrl(imageUrl);
        }
        combo = comboRepository.save(combo);
        List<Long> packageIds = comboDto.getPackageIds();
        for (Long packageId : packageIds){
            Combo finalCombo = combo;
            packageRepository.findById(packageId).ifPresent(aPackage -> {
                PackageCombo packageCombo = new PackageCombo();
                packageCombo.setCombo(finalCombo);
                packageCombo.setAPackage(aPackage);
                packageComboRepository.save(packageCombo);
            });
        }
        return new Response<>("Combo registrado", false, 200, "Combo registrado correctamente, con su imagen");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> updateCombo(String encryptedData) throws Exception {
        String decryptedDataJson = encryptionService.decrypt(encryptedData);
        ComboDto comboDto = objectMapper.readValue(decryptedDataJson, ComboDto.class);
        Long comboId = Long.parseLong(comboDto.getComboId());
        Combo existingCombo = comboRepository.findById(comboId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID"));
        existingCombo.setComboName(comboDto.getComboName());
        existingCombo.setComboDescription(comboDto.getComboDescription());
        existingCombo.setComboPrice(comboDto.getComboPrice());
        existingCombo.setComboDesignatedHours(comboDto.getComboDesignatedHours());
        existingCombo.setComboWorkersNumber(comboDto.getComboWorkersNumber());
        comboRepository.save(existingCombo);
        return new Response<>("Combo actualizado", false, HttpStatus.OK.value(), "Combo actualizado correctamente");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<String> deleteCombo(String encryptedData) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        ComboDto comboDto = objectMapper.readValue(decryptedDataJson, ComboDto.class);
        Long comboId = Long.parseLong(comboDto.getComboId());
        Combo combo = comboRepository.findById(comboId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado"));

        if (!combo.getOrderCombos().isEmpty()) {
            return new Response<>(null, true, 400, "No se puede eliminar el combo porque ha sido asociado con ordenes.");
        }

        try {
            firebaseStorageService.deleteFileFromFirebase(combo.getComboImgUrl(), "combos-pics/");
        } catch (IOException e) {
            e.printStackTrace();
            return new Response<>(null, true, 500, "Error al eliminar la imagen en Firebase"
            );
        }
        comboRepository.deleteById(combo.getComboId());
        return new Response<>("Servicio eliminado", false, 200, "Servicio eliminado con exito.");
    }

    public List<Package> getPackagesByComboId(Long comboId) {
        List<PackageCombo> packageCombos = packageComboRepository.findByComboComboId(comboId);
        return packageCombos.stream().map(PackageCombo::getAPackage).collect(Collectors.toList());
    }
}
