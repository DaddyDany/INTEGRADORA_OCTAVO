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
    public ResponseEntity getAll() {
        return new ResponseEntity(
                this.orderService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.orderService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Response<Order>> insert(
            @Valid @RequestBody OrderDto order
    ) {
        return new ResponseEntity<>(
                this.orderService.insertOrder(order.getOrder()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @RequestBody OrderDto order
    ) {
        return new ResponseEntity(
                this.orderService.updateOrder(order.getOrder()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity(
                this.orderService.deleteOrder(id),
                HttpStatus.OK
        );
    }
}
