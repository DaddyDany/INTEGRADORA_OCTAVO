package utez.edu.mx.orderapp.controllers.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderInfoDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.services.orders.OrderService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService orderService;
    private final CommonUserRepository commonUserRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public OrderController(OrderService orderService, CommonUserRepository commonUserRepository, WorkerRepository workerRepository){
        this.orderService = orderService;
        this.commonUserRepository = commonUserRepository;
        this.workerRepository = workerRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderInfoDto>> getAll() {
        Response<List<OrderInfoDto>> response = orderService.getAll();
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable("id") Long id) {
        Response<Order> response = orderService.getOne(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PostMapping
    public ResponseEntity<Response<String>> createOrder(@RequestBody String encryptedData, Authentication authentication) throws Exception {
        String username = authentication.getName();
        CommonUser commonUser = commonUserRepository.findByUserEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Long userId = commonUser.getCommonUserId();
        Response<String> response = orderService.createOrder(encryptedData, userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        CommonUser commonUser = commonUserRepository.findById(orderDto.getCommonUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Order order = orderDto.toOrder(commonUser);
        order.setOrderId(id);
        Response<Order> response = this.orderService.updateOrder(order);
        if (response.isSuccess()){
            return ResponseEntity.ok(response.getData());
        } else {
            return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response.getMessage());
        }
    }

    @PatchMapping("/decline")
    public ResponseEntity<Response<String>> declineOrder(@RequestBody String encryptedData) throws Exception {
        Response<String> response = orderService.declineOrder(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PatchMapping("/accept-and-assign")
    public ResponseEntity<Response<String>> acceptAndAssignOrder(@RequestBody String encryptedData) throws Exception {
        Response<String> response = orderService.acceptAndAssignWorkers(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PatchMapping("/complete")
    public ResponseEntity<Response<String>> markAsOrderCompleted(@RequestBody String encryptedData) throws Exception {
        Response<String> response = orderService.completeOrder(encryptedData);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> delete(@PathVariable("id") Long id) {
        Response<Order> response = this.orderService.deleteOrder(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderInfoDto>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        Optional<CommonUser> user = commonUserRepository.findByUserEmail(username);
        if (user.isPresent()) {
            List<OrderInfoDto> orders = orderService.findOrdersByUserId(user.get().getCommonUserId());
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/worker-assigned-orders")
    public ResponseEntity<List<OrderInfoDto>> getWorkerOrders(Authentication authentication) {
        String username = authentication.getName();
        Optional<Worker> user = workerRepository.findByWorkerEmail(username);
        if (user.isPresent()) {
            List<OrderInfoDto> orders = orderService.findOrdersByWorkerId(user.get().getWorkerId());
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
