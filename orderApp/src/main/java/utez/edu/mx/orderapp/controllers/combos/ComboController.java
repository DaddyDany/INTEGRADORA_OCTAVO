package utez.edu.mx.orderapp.controllers.combos;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.orderapp.controllers.combos.dtos.ComboDto;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.services.combos.ComboService;
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
    public ResponseEntity<List<Combo>> getAll() {
        Response<List<Combo>> response = this.comboService.getAll();
        if (response.isSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Combo> getOne(@PathVariable("id") Long id) {
        Response<Combo> response = this.comboService.getOne(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PostMapping
    public ResponseEntity<Response<Long>> insertCombo(@ModelAttribute ComboDto comboDto) {
        Response<Long> response = this.comboService.insertCombo(comboDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ComboDto comboDto) {
        Response<Combo> response = comboService.updateCombo(id, comboDto);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return ResponseEntity
                    .status(HttpStatus.valueOf(response.getStatus()))
                    .body(response.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Combo> delete(@PathVariable("id") Long id) {
        Response<Combo> response = this.comboService.deleteCombo(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{comboId}/packages")
    public List<Package> getPackagesByComboId(@PathVariable Long comboId) {
        return comboService.getPackagesByComboId(comboId);
    }
}
