package utez.edu.mx.orderapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.repositories.orders.OrderRepository;
import utez.edu.mx.orderapp.utils.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public Response<List<Order>> getAll() {
        return new Response<>(
                this.orderRepository.findAll(),
                false,
                200,
                "OK"
        );
    }
    @Transactional(readOnly = true)
    public Response<Order> getOne(long id) {
        Optional<Order> order = this.orderRepository.findById(id);
        return order.map(value -> new Response<>(value, false, HttpStatus.OK.value(), "Order fetched successfully"))
                .orElseGet(() -> new Response<>(true, HttpStatus.NOT_FOUND.value(), "Order not found"));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Order> insertOrder(Order order) {
        if (this.orderRepository.existsByOrderDate(order.getOrderDate()))
            return new Response<>(
                    null,
                    true,
                    200,
                    "Ya hay una order para este día, intente con otra fecha"
            );
        return new Response<>(
                this.orderRepository.saveAndFlush(order),
                false,
                200,
                "Orden registrada correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Order> updateOrder(Order order) {
        if (this.orderRepository.existsById(order.getOrderId()))
            return new Response<>(
                    this.orderRepository.saveAndFlush(order),
                    false,
                    200,
                    "Orden actualizada correctamente"
            );
        return new Response<>(
                null,
                true,
                200,
                "No existe la order solicitada"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response<Order> deleteOrder(Long id) {
        if (this.orderRepository.existsById(id)) {
            this.orderRepository.deleteById(id);
            return new Response<>(
                    null,
                    false,
                    200,
                    "Orden eliminada correctamente"
            );
        }
        return new Response<>(
                null,
                true,
                200,
                "No existe la orden solicitada"
        );
    }
}
