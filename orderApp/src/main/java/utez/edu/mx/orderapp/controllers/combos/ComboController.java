package utez.edu.mx.orderapp.controllers.combos;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.models.categories.Category;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.services.ComboService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/api/combos")
@CrossOrigin(origins = "http://localhost:5173")
public class ComboController {
    private final ComboService comboService;
    @Autowired
    public ComboController(ComboService comboService){
        this.comboService = comboService;
    }
    @GetMapping
    public ResponseEntity<Response<List<Combo>>> getAll() {
        Response<List<Combo>> combos = this.comboService.getAll();
        return new ResponseEntity<>(
                combos,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Combo>> getOne(
            @PathVariable("id") Long id
    ) {
        Response<Combo> combo = this.comboService.getOne(id);
        return new ResponseEntity<>(
                combo,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Response<Combo>> insert(
            @Valid @RequestBody ComboDto comboDto
    ) {
        return new ResponseEntity<>(
                this.comboService.insertCombo(comboDto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Combo>> update(
            @RequestBody ComboDto combo
    ) {
        return new ResponseEntity<>(
                this.comboService.updateCombo(combo.getCombo()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Combo>> delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                this.comboService.deleteCombo(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/{comboId}/packages")
    public List<Package> getPackagesByComboId(@PathVariable Long comboId) {
        return comboService.getPackagesByComboId(comboId);
    }
}
