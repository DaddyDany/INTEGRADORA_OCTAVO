package utez.edu.mx.orderapp.services.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderAcceptanceDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderInfoDto;
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
import utez.edu.mx.orderapp.utils.EncryptionService;
import utez.edu.mx.orderapp.utils.Response;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
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
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;
    private final OrderMapper orderMapper;
    @Autowired
    public OrderService(OrderRepository orderRepository, CommonUserRepository commonUserRepository, PackageRepository packageRepository, ComboRepository comboRepository, WorkerRepository workerRepository, WorkerOrderRepository workerOrderRepository, EncryptionService encryptionService, ObjectMapper objectMapper, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.commonUserRepository = commonUserRepository;
        this.packageRepository = packageRepository;
        this.comboRepository = comboRepository;
        this.workerRepository = workerRepository;
        this.workerOrderRepository = workerOrderRepository;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public Response<List<OrderInfoDto>> getAll() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderInfoDto> dtoList = orders.stream().map(order -> {
            try {
                return this.orderMapper.toOrderInfoDto(order);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();
        return new Response<>(dtoList, false, 200, "OK");
    }

    @Transactional(readOnly = true)
    public Response<Order> getOne(long id) {
        Optional<Order> order = this.orderRepository.findById(id);
        return order.map(value -> new Response<>(value, false, HttpStatus.OK.value(), "Order fetched successfully"))
                .orElseGet(() -> new Response<>(true, HttpStatus.NOT_FOUND.value(), "Order not found"));
    }

    @Transactional
    public Response<String> createOrder(String encryptedData, Long userId) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        OrderDto orderDto = objectMapper.readValue(decryptedDataJson, OrderDto.class);

        Stripe.apiKey = stripeSecretKey;
        try {
            Session.retrieve(orderDto.getSessionId());
        } catch (StripeException e) {
            return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Session ID no válido o problema al comunicarse con Stripe: " + e.getMessage());
        }

        try {
            CommonUser commonUser = commonUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Order order = new Order();
            Optional<Order> existingOrder = orderRepository.findBySessionId(orderDto.getSessionId());

            if (existingOrder.isPresent()) {
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
            int totalWorkers = 0;
            String orderType = "Orden de paquete personalizado";
            if (!orderDto.getPackagesIds().isEmpty()) {
                if (orderDto.getPackagesIds().size() > 1){
                    totalHours = 5;
                }
                for (Long packageId : orderDto.getPackagesIds()) {
                    Package pkg = packageRepository.findById(packageId)
                            .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));
                    totalPayment += pkg.getPackagePrice();
                    totalWorkers += pkg.getWorkersNumber();
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
                    totalWorkers += combo.getComboWorkersNumber();
                    if (orderDto.getCombosIds().size() == 1){
                        totalHours = combo.getComboDesignatedHours();
                    }
                    OrderCombo orderCombo = new OrderCombo(order, combo);
                    order.getOrderCombos().add(orderCombo);
                }
            }
            order.setOrderTotalPayment(totalPayment);
            order.setOrderTotalWokers(totalWorkers);
            order.setOrderTotalHours(totalHours);
            order.setOrderType(orderType);
            orderRepository.save(order);
            return new Response<>("Registrada", false, HttpStatus.CREATED.value(), "Orden creada con éxito");
        } catch (Exception e) {
            return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al crear la orden: " + e.getMessage());
        }
    }

    @Transactional
    public Response<String> declineOrder(String encryptedData) throws Exception{
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        OrderDto orderDto = objectMapper.readValue(decryptedDataJson, OrderDto.class);
        Order order = orderRepository.findById(Long.parseLong(orderDto.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        if (Objects.equals(order.getOrderState(), "Declinada")) {
            return new Response<>(null, true, 400, "La orden ya ha sido declinada, no hace falta hacerlo dos veces.");
        }
        if (Objects.equals(order.getOrderState(), "Orden completada :)")) {
            return new Response<>(null, true, 400, "La orden ya ha sido completada, no puedes marcarla como declinada.");
        }
        Stripe.apiKey = stripeSecretKey;
        try {
            Session session = Session.retrieve(order.getSessionId());
            String paymentIntentId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            if (!"succeeded".equals(paymentIntent.getStatus())) {
                paymentIntent.cancel();
                order.setOrderPaymentState("Cancelado");
            } else {
                return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "El pago ya ha sido capturado, no es posible cancelar");
            }
        } catch (StripeException e) {
            return new Response<>(null, true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Stripe error: " + e.getMessage());
        }
        order.setOrderState("Declinada");
        orderRepository.save(order);
        return new Response<>("Declinación", false, HttpStatus.CREATED.value(), "Orden declinada con éxito");
    }

    @Transactional
    public Response<String> acceptAndAssignWorkers(String encryptedData) throws Exception {
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        OrderAcceptanceDto orderAcceptanceDto = objectMapper.readValue(decryptedDataJson, OrderAcceptanceDto.class);
        Optional<Order> orderOptional = orderRepository.findById(orderAcceptanceDto.getOrderId());

        if (orderOptional.isEmpty()) {
            return new Response<>(null, true, 404, "Orden no encontrada.");
        }

        if (Objects.equals(orderOptional.get().getOrderState(), "Declinada")) {
            return new Response<>(null, true, 400, "La orden ya ha sido declinada, no puedes aceptarla ahora.");
        }

        if (Objects.equals(orderOptional.get().getOrderState(), "Aceptada")) {
            return new Response<>(null, true, 400, "La orden ya ha sido aceptada, no hace falta hacerlo dos veces.");
        }
        if (Objects.equals(orderOptional.get().getOrderState(), "Orden completada :)")) {
            return new Response<>(null, true, 400, "La orden ya ha sido completada, no puedes marcarla como aceptada nuevamente.");
        }
        Order order = orderOptional.get();
        LocalDate orderDate = order.getOrderDate();
        LocalTime orderStartTime = order.getOrderTime();
        LocalTime orderEndTime = orderStartTime.plusHours(order.getOrderTotalHours());
        int totalWorkers = order.getOrderTotalWokers();

        if (orderAcceptanceDto.getWorkerIds().size() != totalWorkers){
            return new Response<>(null, true, 400, "Los trabajadores seleccionados no cubren la cuota de los solicitados, hacen falta por seleccionar: " + (totalWorkers - orderAcceptanceDto.getWorkerIds().size()) + " trabajadores");
        }

        for (Long workerId : orderAcceptanceDto.getWorkerIds()) {
            Worker worker = workerRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado."));
            if (!isWorkerAvailable(workerId, orderDate, orderStartTime, orderEndTime)) {
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
        String sessionId = orderOptional.get().getSessionId();
        Stripe.apiKey = stripeSecretKey;
        try {
            Session session = Session.retrieve(sessionId);
            String paymentIntentId = session.getPaymentIntent();
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
            intent.capture();
        } catch (StripeException e) {
            e.printStackTrace();
            return new Response<>(null, true, 500, "Error al capturar el pago: " + e.getMessage());
        }
        order.setOrderPaymentState("Capturado");
        orderRepository.save(order);

        return new Response<>("Orden aceptada y trabajadores asignados exitosamente.", false, 200, "Éxito");
    }


    private boolean isWorkerAvailable(Long workerId, LocalDate orderDate, LocalTime startTime, LocalTime endTime) {
        List<WorkerOrder> assignmentsForDay = workerOrderRepository.findByWorkerIdAndOrderDate(workerId, orderDate);

        long totalAssignedHoursForDay = assignmentsForDay.stream()
                .mapToLong(assignment -> Duration.between(assignment.getStartTime(), assignment.getEndTime()).toHours())
                .sum();
        long hoursToAssign = Duration.between(startTime, endTime).toHours();
        if (totalAssignedHoursForDay + hoursToAssign > 8) {
            return false;
        }
        for (WorkerOrder assignment : assignmentsForDay) {
            LocalTime existingAssignmentStart = assignment.getStartTime();
            LocalTime existingAssignmentEnd = assignment.getEndTime();
            boolean overlaps = !startTime.isAfter(existingAssignmentEnd.plusMinutes(30)) && !endTime.plusMinutes(30).isBefore(existingAssignmentStart);
            if (overlaps) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Response<String> completeOrder(String encryptedData) throws Exception {
        String correctedData = encryptedData.replace("\"", "");
        String decryptedDataJson = encryptionService.decrypt(correctedData);
        OrderDto orderDto = objectMapper.readValue(decryptedDataJson, OrderDto.class);
        Order order = orderRepository.findById(Long.parseLong(orderDto.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        if (Objects.equals(order.getOrderState(), "Orden completada :)")) {
            return new Response<>(null, true, 400, "La orden ya ha sido servida, no la marques dos veces.");
        }
        if (Objects.equals(order.getOrderState(), "Declinada")) {
            return new Response<>(null, true, 400, "La orden ya ha sido declinada, no puedes marcarla como servida.");
        }
        if (Objects.equals(order.getOrderState(), "Pendiente")) {
            return new Response<>(null, true, 400, "La orden debe ser aceptada primero");
        }

        order.setOrderState("Orden completada :)");
        orderRepository.save(order);
        return new Response<>("Completada", false, HttpStatus.CREATED.value(), "La orden ha sido marcada como completada con exito");
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

    public List<OrderInfoDto> findOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByCommonUserCommonUserId(userId);
        return orders.stream()
                .map(order -> {
                    try {
                        return this.orderMapper.toOrderInfoDto(order);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<OrderInfoDto> findOrdersByWorkerId(Long userId) {
        List<Order> orders = orderRepository.findByWorkerId(userId);
        return orders.stream()
                .map(order -> {
                    try {
                        return this.orderMapper.toOrderInfoDto(order);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
