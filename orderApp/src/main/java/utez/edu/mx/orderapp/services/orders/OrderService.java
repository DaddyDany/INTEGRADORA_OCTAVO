package utez.edu.mx.orderapp.services.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.orders.OrderDto;
import utez.edu.mx.orderapp.controllers.orders.OrderInfoAdminDto;
import utez.edu.mx.orderapp.controllers.orders.OrderResponseDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.orders.OrderPackage;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.combos.ComboRepository;
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
    private final ComboRepository comboRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, CommonUserRepository commonUserRepository, PackageRepository packageRepository, ComboRepository comboRepository){
        this.orderRepository = orderRepository;
        this.commonUserRepository = commonUserRepository;
        this.packageRepository = packageRepository;
        this.comboRepository = comboRepository;
    }

    @Transactional(readOnly = true)
    public Response<List<OrderInfoAdminDto>> getAll() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderInfoAdminDto> dtoList = orders.stream().map(OrderMapper::toOrderInfoAdminDto).toList();
        return new Response<>(dtoList, false, 200, "OK");
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
            String orderType = "Orden de paquete personalizado";
            if (!orderDto.getPackagesIds().isEmpty()) {
                if (orderDto.getPackagesIds().size() > 1){
                    totalHours = 5;
                }
                for (Long packageId : orderDto.getPackagesIds()) {
                    Package pkg = packageRepository.findById(packageId)
                            .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));
                    totalPayment += pkg.getPackagePrice();
                    if (orderDto.getPackagesIds().size() == 1) {
                        totalHours += pkg.getDesignatedHours();
                    }
                    OrderPackage orderPackage = new OrderPackage(order, pkg);
                    order.getOrderPackages().add(orderPackage);
                }
                orderType = orderDto.getPackagesIds().size() == 1 ? "Orden de paquete individual" : "Orden de paquete personalizado";
            }

            if (!orderDto.getCombosIds().isEmpty()) {
                orderType = "Orden de combo";
                for (Long comboId : orderDto.getCombosIds()){
                    Combo combo = comboRepository.findById(comboId)
                            .orElseThrow(() -> new RuntimeException("Combo no encontrado"));
                    totalPayment += combo.getComboPrice();
                    if (orderDto.getCombosIds().size() == 1){
                        totalHours = combo.getComboDesignatedHours();
                    }
                    OrderCombo orderCombo = new OrderCombo(order, combo);
                    order.getOrderCombos().add(orderCombo);
                }
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

    @Transactional
    public void declineOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        order.setOrderState("Declinado");
        order.setOrderPaymentState("Devolucion");
        orderRepository.save(order);
    }

    @Transactional
    public void acceptOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        order.setOrderState("Aceptada");
        order.setOrderPaymentState("Cobrada");
        orderRepository.save(order);
    }

    @Transactional
    public void completeOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        order.setOrderState("Servida");
        orderRepository.save(order);
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

    public List<OrderResponseDto> findOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByCommonUserCommonUserId(userId);
        return orders.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }
}
