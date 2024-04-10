package utez.edu.mx.orderapp.services.orders;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderAcceptanceDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderInfoAdminDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderResponseDto;
import utez.edu.mx.orderapp.models.accounts.CommonUser;
import utez.edu.mx.orderapp.models.accounts.Worker;
import utez.edu.mx.orderapp.models.combos.Combo;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.models.orders.OrderCombo;
import utez.edu.mx.orderapp.models.orders.OrderPackage;
import utez.edu.mx.orderapp.models.orders.WorkerOrder;
import utez.edu.mx.orderapp.models.packages.Package;
import utez.edu.mx.orderapp.repositories.accounts.CommonUserRepository;
import utez.edu.mx.orderapp.repositories.accounts.WorkerRepository;
import utez.edu.mx.orderapp.repositories.combos.ComboRepository;
import utez.edu.mx.orderapp.repositories.orders.OrderRepository;
import utez.edu.mx.orderapp.repositories.orders.WorkerOrderRepository;
import utez.edu.mx.orderapp.repositories.packages.PackageRepository;
import utez.edu.mx.orderapp.utils.Response;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    @Value("${stripe.keys.secret}")
    private String stripeSecretKey;
    private final OrderRepository orderRepository;
    private final CommonUserRepository commonUserRepository;
    private final PackageRepository packageRepository;
    private final ComboRepository comboRepository;
    private final WorkerRepository workerRepository;
    private final WorkerOrderRepository workerOrderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, CommonUserRepository commonUserRepository, PackageRepository packageRepository, ComboRepository comboRepository, WorkerRepository workerRepository, WorkerOrderRepository workerOrderRepository){
        this.orderRepository = orderRepository;
        this.commonUserRepository = commonUserRepository;
        this.packageRepository = packageRepository;
        this.comboRepository = comboRepository;
        this.workerRepository = workerRepository;
        this.workerOrderRepository = workerOrderRepository;
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
    public Response<String> createOrder(OrderDto orderDto) {

        Stripe.apiKey = stripeSecretKey;
        try {
            Session session = Session.retrieve(orderDto.getSessionId());
        } catch (StripeException e) {
            return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Session ID no válido o problema al comunicarse con Stripe: " + e.getMessage());
        }

        try {
            CommonUser commonUser = commonUserRepository.findById(orderDto.getCommonUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Order order = new Order();
            Optional<Order> existingOrder = orderRepository.findBySessionId(orderDto.getSessionId());

            if (existingOrder.isPresent()) {
                // Si ya existe una orden con ese sessionId, devuelve el estado HTTP 466 con el mensaje
                return new Response<>("Mucho ojo", true, HttpStatus.FORBIDDEN.value(), "Tu orden ya fue registrada");
            }
            order.setOrderDate(orderDto.getOrderDate());
            order.setSessionId(orderDto.getSessionId());
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
            return new Response<>("Registrada", false, HttpStatus.CREATED.value(), "Orden creada con éxito");
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
    public Response<String> acceptAndAssignWorkers(OrderAcceptanceDto dto) {
        Optional<Order> orderOptional = orderRepository.findById(dto.getOrderId());
        if (orderOptional.isEmpty()) {
            return new Response<>(null, true, 404, "Orden no encontrada.");
        }

        Order order = orderOptional.get();
        LocalDateTime orderStartTime = order.getOrderTime();
        LocalDateTime orderEndTime = orderStartTime.plusHours(order.getOrderTotalHours());

        for (Long workerId : dto.getWorkerIds()) {
            Worker worker = workerRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado."));
            if (!isWorkerAvailable(workerId, orderStartTime, orderEndTime)) {
                return new Response<>(null, true, 400, "El trabajador " + workerId + " no está disponible en el horario requerido.");
            }
            WorkerOrder workerOrder = new WorkerOrder();
            workerOrder.setWorker(worker);
            workerOrder.setOrder(order);
            workerOrder.setAssignedHours(order.getOrderTotalHours());
            workerOrder.setStartTime(orderStartTime);
            workerOrder.setEndTime(orderEndTime);
            workerOrderRepository.save(workerOrder);
        }
        order.setOrderState("Aceptada");
        orderRepository.save(order);

        return new Response<>("Orden aceptada y trabajadores asignados exitosamente.", false, 200, "Éxito");
    }


    private boolean isWorkerAvailable(Long workerId, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate date = startTime.toLocalDate();
        List<WorkerOrder> assignmentsForDay = workerOrderRepository.findByWorkerIdAndDate(workerId, date);

        long totalAssignedHoursForDay = assignmentsForDay.stream()
                .mapToLong(assignment -> Duration.between(assignment.getStartTime(), assignment.getEndTime()).toHours())
                .sum();
        long hoursToAssign = Duration.between(startTime, endTime).toHours();
        if (totalAssignedHoursForDay + hoursToAssign > 8) {
            return false;
        }
        for (WorkerOrder assignment : assignmentsForDay) {
            LocalDateTime existingAssignmentStart = assignment.getStartTime();
            LocalDateTime existingAssignmentEnd = assignment.getEndTime();
            boolean overlaps = !startTime.isAfter(existingAssignmentEnd.plusMinutes(30)) && !endTime.plusMinutes(30).isBefore(existingAssignmentStart);
            if (overlaps) {
                return false;
            }
        }
        return true;
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
