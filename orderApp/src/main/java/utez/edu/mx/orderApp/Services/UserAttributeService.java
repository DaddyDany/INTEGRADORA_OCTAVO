package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.UserAttribute;
import utez.edu.mx.orderApp.Repositories.UserAttributeRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

@Service
public class UserAttributeService {
    @Autowired
    private UserAttributeRepository userAttributeRepository;

    public Response<List<UserAttribute>> getAll() {
        return new Response<>(
                this.userAttributeRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }


    public Response delete(Long id) {
        if (this.userAttributeRepository.existsById(id)) {
            this.userAttributeRepository.deleteById(id);
            return new Response(
                    null,
                    false,
                    200,
                    "Usuario eliminado correctamente"
            );
        }
        return new Response(
                null,
                true,
                200,
                "No existe el usuario indicado"
        );
    }
}
