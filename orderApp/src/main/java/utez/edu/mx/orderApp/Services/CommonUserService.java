package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.CommonUser;
import utez.edu.mx.orderApp.Repositories.CommonUserRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

@Service
public class CommonUserService {
    @Autowired
    private CommonUserRepository commonUserRepository;

    public Response getAll(){
        return new Response<List<CommonUser>>(
                this.commonUserRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    public Response getOne(long commonUserId) {
        return new Response<Object>(
                this.commonUserRepository.findById(commonUserId),
                false,
                200,
                "Ok"
        );
    }
}
