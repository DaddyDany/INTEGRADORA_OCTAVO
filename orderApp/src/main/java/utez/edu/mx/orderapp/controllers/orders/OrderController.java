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
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.services.OrderService;
import utez.edu.mx.orderapp.utils.Response;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
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
    public ResponseEntity<Order> insert(@Valid @RequestBody OrderDto order) {
        Response<Order> response = this.orderService.insertOrder(order.toOrder());
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        Order order = orderDto.toOrder();
        order.setOrderId(id);
        Response<Order> response = this.orderService.updateOrder(order);
        if (response.isSuccess()){
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(response.getStatus()));
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
