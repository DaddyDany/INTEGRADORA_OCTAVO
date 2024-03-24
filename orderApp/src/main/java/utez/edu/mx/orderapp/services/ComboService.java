package utez.edu.mx.orderapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.combos.ComboDto;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.models.packages.PackageCombo;
import utez.edu.mx.orderapp.repositories.combos.ComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageComboRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.utils.Response;

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

    @Autowired
    public ComboService(ComboRepository comboRepository, PackageComboRepository packageComboRepository, PackageRepository packageRepository){
        this.comboRepository = comboRepository;
        this.packageComboRepository = packageComboRepository;
        this.packageRepository = packageRepository;
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
    public Response<Combo> insertCombo(ComboDto comboDto) {
        Combo combo = comboDto.getCombo();
        Combo savedCombo = this.comboRepository.saveAndFlush(combo);
        List<Long> packageIds = comboDto.getPackageIds();
        for (Long packageId : packageIds) {
            packageRepository.findById(packageId).ifPresent(aPackage -> {
                PackageCombo packageCombo = new PackageCombo();
                packageCombo.setCombo(savedCombo);
                packageCombo.setAPackage(aPackage);
                packageComboRepository.save(packageCombo);
            });
        }
        return new Response<>(
                savedCombo,
                false,
                200,
                "Combo registrado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Combo> updateCombo(Combo combo) {
        if (this.comboRepository.existsById(combo.getComboId()))
            return new Response<>(
                    this.comboRepository.saveAndFlush(combo),
                    false,
                    200,
                    "Combo actualizado correctamente"
            );
        return new Response<>(
                null,
                true,
                200,
                "No existe el combo buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Combo> deleteCombo(Long id) {
        if (this.comboRepository.existsById(id)) {
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
                "No existe el combo buscado"
        );
    }

    public List<Package> getPackagesByComboId(Long comboId) {
        List<PackageCombo> packageCombos = packageComboRepository.findByComboComboId(comboId);
        return packageCombos.stream().map(PackageCombo::getAPackage).collect(Collectors.toList());
    }
}
