package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.Role;
import utez.edu.mx.orderApp.Repositories.RoleRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Response getAll(){
        return new Response<List<Role>>(
                this.roleRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    public Response getOne(long roleId) {
        return new Response<Object>(
                this.roleRepository.findById(roleId),
                false,
                200,
                "Ok"
        );
    }

    public Response insert(Role role) {
        if (this.roleRepository.existsByRoleName(role.getRoleName()))
            return new Response(
                    null,
                    true,
                    200,
                    "Ya existe ese rol"
            );
        return new Response(
                this.roleRepository.saveAndFlush(role),
                false,
                200,
                "Rol registrado correctamente"
        );
    }

    public Response update(Role role) {
        if (this.roleRepository.existsByRoleName(role.getRoleName()))
            return new Response(
                    this.roleRepository.saveAndFlush(role),
                    false,
                    200,
                    "Rol actualizado correctamente"
            );
        return new Response(
                null,
                true,
                200,
                "No existe el rol indicado"
        );
    }

    public Response delete(Long roleId) {
        if (this.roleRepository.existsById(roleId)) {
            this.roleRepository.deleteById(roleId);
            return new Response(
                    null,
                    false,
                    200,
                    "Rol eliminado correctamente"
            );
        }
        return new Response(
                null,
                true,
                200,
                "No existe el role indicado"
        );
    }
}
