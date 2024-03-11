package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Models.Combos.Combo;
import utez.edu.mx.orderApp.Models.Packages.Package;
import utez.edu.mx.orderApp.Repositories.Combos.ComboRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
public class ComboService {
    @Autowired
    private ComboRepository comboRepository;

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
    public Response insertCombo(Combo combo) {
        if (this.comboRepository.existsByComboName(combo.getComboName()))
            return new Response(
                    null,
                    true,
                    200,
                    "Ya existe este combo"
            );
        return new Response(
                this.comboRepository.saveAndFlush(combo),
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
}
