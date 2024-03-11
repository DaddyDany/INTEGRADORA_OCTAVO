package utez.edu.mx.orderApp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderApp.Models.Orders.Order;
import utez.edu.mx.orderApp.Repositories.Orders.OrderRepository;
import utez.edu.mx.orderApp.Utils.Response;
import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Response getAll() {
        return new Response<List<Order>>(
                this.orderRepository.findAll(),
                false,
                200,
                "OK"
        );
    }

    @Transactional(readOnly = true)
    public Response getOne(long id) {
        return new Response<Object>(
                this.orderRepository.findById(id),
                false,
                200,
                "OK"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response insertOrder(Order order) {
        if (this.orderRepository.existsByOrderDate(order.getOrderDate()))
            return new Response(
                    null,
                    true,
                    200,
                    "Ya hay una order para este día, intente con otra fecha"
            );
        return new Response(
                this.orderRepository.saveAndFlush(order),
                false,
                200,
                "Orden registrada correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response updateOrder(Order order) {
        if (this.orderRepository.existsById(order.getOrderId()))
            return new Response(
                    this.orderRepository.saveAndFlush(order),
                    false,
                    200,
                    "Orden actualizada correctamente"
            );
        return new Response(
                null,
                true,
                200,
                "No existe la order solicitada"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Response deleteOrder(Long id) {
        if (this.orderRepository.existsById(id)) {
            this.orderRepository.deleteById(id);
            return new Response(
                    null,
                    false,
                    200,
                    "Orden eliminada correctamente"
            );
        }
        return new Response(
                null,
                true,
                200,
                "No existe la orden solicitada"
        );
    }
}
