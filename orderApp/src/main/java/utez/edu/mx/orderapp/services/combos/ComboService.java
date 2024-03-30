package utez.edu.mx.orderapp.services.combos;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.combos.dtos.ComboDto;
import utez.edu.mx.orderapp.firebaseintegrations.FirebaseStorageService;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.packages.ImagePackage;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.models.packages.PackageCombo;
import utez.edu.mx.orderapp.repositories.combos.ComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
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

    @Autowired
    public ComboService(ComboRepository comboRepository, PackageComboRepository packageComboRepository, PackageRepository packageRepository, FirebaseStorageService firebaseStorageService){
        this.comboRepository = comboRepository;
        this.packageComboRepository = packageComboRepository;
        this.packageRepository = packageRepository;
        this.firebaseStorageService = firebaseStorageService;
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
    public Response<Long> insertCombo(ComboDto comboDto) {
        try{
            Combo combo = new Combo();
            combo.setComboName(comboDto.getComboName());
            combo.setComboDescription(comboDto.getComboDescription());
            combo.setComboPrice(comboDto.getComboPrice());
            combo.setComboDesignatedHours(comboDto.getComboDesignatedHours());
            combo.setComboWorkersNumber(comboDto.getComboWorkersNumber());

            if (comboDto.getComboImg() != null && !comboDto.getComboImg().isEmpty()){
                String imageUrl = firebaseStorageService.uploadFile(comboDto.getComboImg(), "combos-pics/");
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
            return new Response<>(combo.getComboId(), false, 200, "Combo registrado correctamente, con su imagen");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Combo> updateCombo(Long id, ComboDto comboDto) {
        Combo existingCombo = comboRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + id));
        existingCombo.setComboName(comboDto.getComboName());
        existingCombo.setComboDescription(comboDto.getComboDescription());
        existingCombo.setComboPrice(comboDto.getComboPrice());
        existingCombo.setComboDesignatedHours(comboDto.getComboDesignatedHours());
        existingCombo.setComboWorkersNumber(comboDto.getComboWorkersNumber());
        comboRepository.save(existingCombo);
        return new Response<>(existingCombo, false, HttpStatus.OK.value(), "Combo actualizado correctamente");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Combo> deleteCombo(Long id) {
        Optional<Combo> comboOptional = this.comboRepository.findById(id);
        if (comboOptional.isPresent()) {
            Combo combo = comboOptional.get();
            try{
                firebaseStorageService.deleteFileFromFirebase(combo.getComboImgUrl(), "combos-pict/");
            }catch (IOException e){
                e.printStackTrace();
                return new Response<>(
                        null,
                        true,
                        500,
                        "Error al eliminar imágenes en Firebase"
                );
            }
            this.comboRepository.deleteById(id);
            return new Response<>(
                    null,
                    false,
                    200,
                    "Combo eliminado correctamente"
            );
        }
        return new Response<>(
                null,
                true,
                200,
                "No existe el Combo buscado"
        );
    }

    public List<Package> getPackagesByComboId(Long comboId) {
        List<PackageCombo> packageCombos = packageComboRepository.findByComboComboId(comboId);
        return packageCombos.stream().map(PackageCombo::getAPackage).collect(Collectors.toList());
    }
}
