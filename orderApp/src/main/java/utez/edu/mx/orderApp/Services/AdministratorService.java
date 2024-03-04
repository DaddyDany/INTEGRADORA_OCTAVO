package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.Administrator;
import utez.edu.mx.orderApp.Repositories.AdministratorRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorRepository administratorRepository;

    public Response getAll(){
        return new Response<List<Administrator>>(
                this.administratorRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    public Response getOne(long roleId) {
        return new Response<Object>(
                this.administratorRepository.findById(roleId),
                false,
                200,
                "Ok"
        );
    }

}
