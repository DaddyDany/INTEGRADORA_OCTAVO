package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderApp.Models.Worker;
import utez.edu.mx.orderApp.Repositories.WorkerRepository;
import utez.edu.mx.orderApp.Utils.Response;

import java.util.List;

@Service
public class WorkerService {
    @Autowired
    public WorkerRepository workerRepository;

    public Response getAll(){
        return new Response<List<Worker>>(
                this.workerRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    public Response getOne(long workerId) {
        return new Response<Object>(
                this.workerRepository.findById(workerId),
                false,
                200,
                "Ok"
        );
    }
}
