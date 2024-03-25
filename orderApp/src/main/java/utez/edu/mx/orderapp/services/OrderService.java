package utez.edu.mx.orderapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.orders.OrderDto;
import utez.edu.mx.orderapp.controllers.orders.OrderResponseDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.orders.OrderPackage;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.orders.OrderRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.utils.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final CommonUserRepository commonUserRepository;

    private final PackageRepository packageRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, CommonUserRepository commonUserRepository, PackageRepository packageRepository){
        this.orderRepository = orderRepository;
        this.commonUserRepository = commonUserRepository;
        this.packageRepository = packageRepository;
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

    @Transactional
    public Response<OrderResponseDto> createOrder(OrderDto orderDto) {
        try {
            CommonUser commonUser = commonUserRepository.findById(orderDto.getCommonUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Order order = new Order();
            order.setOrderDate(orderDto.getOrderDate());
            order.setOrderPlace(orderDto.getOrderPlace());
            order.setOrderTime(orderDto.getOrderTime());
            order.setCommonUser(commonUser);
            order.setOrderState("Pendiente");
            order.setOrderPaymentState("Pendiente");

            float totalPayment = 0;
            int totalHours = 0;
            String orderType = "Orden de paquete personalizado"; // Valor por defecto

            if (!orderDto.getPackagesIds().isEmpty()) {
                for (Long packageId : orderDto.getPackagesIds()) {
                    Package pkg = packageRepository.findById(packageId)
                            .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));
                    totalPayment += pkg.getPackagePrice();
                    totalHours += pkg.getDesignatedHours();

                    OrderPackage orderPackage = new OrderPackage(order, pkg);
                    order.getOrderPackages().add(orderPackage);
                }
                orderType = orderDto.getPackagesIds().size() == 1 ? "Orden de paquete individual" : "Orden de paquete personalizado";
            }

            if (!orderDto.getCombosIds().isEmpty()) {
                // Suponiendo que los combos también contribuyen al pago total y las horas totales
                // Aquí deberías incluir la lógica para manejar los combos, similar a los paquetes
                orderType = "Orden de combo";
            }

            order.setOrderTotalPayment(totalPayment);
            order.setOrderTotalHours(totalHours);
            order.setOrderType(orderType);

            Order savedOrder = orderRepository.save(order);

            OrderResponseDto responseDto = new OrderResponseDto(
                    savedOrder.getOrderId(),
                    savedOrder.getOrderDate(),
                    savedOrder.getOrderState(),
                    savedOrder.getOrderPlace(),
                    savedOrder.getOrderTime(),
                    savedOrder.getOrderTotalPayment(),
                    savedOrder.getOrderPaymentState(),
                    savedOrder.getOrderType(),
                    savedOrder.getOrderTotalHours(),
                    savedOrder.getCommonUser().getCommonUserId()
            );

            return new Response<>(responseDto, false, HttpStatus.CREATED.value(), "Orden creada con éxito");
        } catch (Exception e) {
            return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al crear la orden: " + e.getMessage());
        }
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
