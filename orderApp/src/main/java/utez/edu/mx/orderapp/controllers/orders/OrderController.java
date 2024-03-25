package utez.edu.mx.orderapp.controllers.orders;

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
import org.springframework.web.server.ResponseStatusException;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.services.OrderService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService orderService;
    private final CommonUserRepository commonUserRepository;

    @Autowired
    public OrderController(OrderService orderService, CommonUserRepository commonUserRepository){
        this.orderService = orderService;
        this.commonUserRepository = commonUserRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        Response<List<Order>> response = this.orderService.getAll();
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable("id") Long id) {
        Response<Order> response = this.orderService.getOne(id);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderDto orderDto) {
        Response<OrderResponseDto> response = orderService.createOrder(orderDto);

        if (!response.isError()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        // Primero, encuentra el CommonUser basado en commonUserId del DTO
        CommonUser commonUser = commonUserRepository.findById(orderDto.getCommonUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Luego, usa ese CommonUser para construir la Order
        Order order = orderDto.toOrder(commonUser);
        order.setOrderId(id);

        // Procesa la actualización con el servicio
        Response<Order> response = this.orderService.updateOrder(order);
        if (response.isSuccess()){
            return ResponseEntity.ok(response.getData());
        } else {
            return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response.getMessage());
        }
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
}
