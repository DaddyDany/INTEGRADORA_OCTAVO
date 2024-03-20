package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Controllers.Combos.ComboDto;
import utez.edu.mx.orderApp.Models.Combos.Combo;
import utez.edu.mx.orderApp.Models.Packages.Package;
import utez.edu.mx.orderApp.Models.Packages.PackageCombo;
import utez.edu.mx.orderApp.Repositories.Combos.ComboRepository;
import utez.edu.mx.orderApp.Repositories.Packages.PackageComboRepository;
import utez.edu.mx.orderApp.Repositories.Packages.PackageRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComboService {

    @Autowired
    private ComboRepository comboRepository;
    @Autowired
    private PackageComboRepository packageComboRepository;
    @Autowired
    private PackageRepository packageRepository;

    @Transactional(readOnly = true)
    public Response getAll() {
        return new Response<List<Combo>>(
                this.comboRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response getOne(long id) {
        return new Response<Object>(
                this.comboRepository.findById(id),
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response insertCombo(ComboDto comboDto) {
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
        return new Response(
                savedCombo,
                false,
                200,
                "Combo registrado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response updateCombo(Combo combo) {
        if (this.comboRepository.existsById(combo.getComboId()))
            return new Response(
                    this.comboRepository.saveAndFlush(combo),
                    false,
                    200,
                    "Combo actualizado correctamente"
            );
        return new Response(
                null,
                true,
                200,
                "No existe el combo buscado"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response deleteCombo(Long id) {
        if (this.comboRepository.existsById(id)) {
            this.comboRepository.deleteById(id);
            return new Response(
                    null,
                    false,
                    200,
                    "Combo eliminado correctamente"
            );
        }
        return new Response(
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
