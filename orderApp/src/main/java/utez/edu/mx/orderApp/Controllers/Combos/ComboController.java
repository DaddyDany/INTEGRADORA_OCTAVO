package utez.edu.mx.orderApp.Controllers.Combos;

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
import utez.edu.mx.orderApp.Models.Combos.Combo;
import utez.edu.mx.orderApp.Services.ComboService;
import utez.edu.mx.orderApp.Utils.Response;

@RestController
@RequestMapping("/api/combos")
@CrossOrigin(origins = "http://localhost:5173")
public class ComboController {
    @Autowired
    private ComboService comboService;
    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.comboService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.comboService.getOne(id),
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
    public ResponseEntity update(
            @RequestBody ComboDto combo
    ) {
        return new ResponseEntity(
                this.comboService.updateCombo(combo.getCombo()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.comboService.deleteCombo(id),
                HttpStatus.OK
        );
    }
}
