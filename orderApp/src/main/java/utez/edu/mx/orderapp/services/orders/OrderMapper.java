package utez.edu.mx.orderapp.services.orders;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderInfoDto;
import utez.edu.mx.orderapp.controllers.orders.dtos.OrderResponseDto;
import utez.edu.mx.orderapp.models.orders.Order;
import utez.edu.mx.orderapp.repositories.orders.OrderComboRepository;
import utez.edu.mx.orderapp.repositories.orders.OrderPackageRepository;
import utez.edu.mx.orderapp.repositories.orders.WorkerOrderRepository;
import utez.edu.mx.orderapp.utils.EncryptionService;

import java.util.List;
import java.util.Objects;

@Service
public class OrderMapper {

    private final EncryptionService encryptionService;
    private final OrderPackageRepository orderPackageRepository;
    private final WorkerOrderRepository workerOrderRepository;
    private final OrderComboRepository orderComboRepository;
    @Autowired
    public OrderMapper(EncryptionService encryptionService, OrderPackageRepository orderPackageRepository, WorkerOrderRepository workerOrderRepository, OrderComboRepository orderComboRepository) {
        this.encryptionService = encryptionService;
        this.orderPackageRepository = orderPackageRepository;
        this.workerOrderRepository = workerOrderRepository;
        this.orderComboRepository = orderComboRepository;
    }
    public OrderResponseDto toOrderResponseDto(Order order) throws Exception {
        OrderResponseDto dto = new OrderResponseDto();

        String id = String.valueOf(order.getOrderId());
        dto.setOrderId(encryptionService.encrypt(id));

        String date = String.valueOf(order.getOrderDate());
        dto.setOrderDate(encryptionService.encrypt(date));

        dto.setOrderState(encryptionService.encrypt(order.getOrderState()));
        dto.setOrderPlace(encryptionService.encrypt(order.getOrderPlace()));

        String time = String.valueOf(order.getOrderTime());
        dto.setOrderTime(encryptionService.encrypt(time));

        String totalPayment = String.valueOf(order.getOrderTotalPayment());
        dto.setOrderTotalPayment(encryptionService.encrypt(totalPayment));

        dto.setOrderPaymentState(encryptionService.encrypt(order.getOrderPaymentState()));
        dto.setOrderType(encryptionService.encrypt(order.getOrderType()));

        String totalHours = String.valueOf(order.getOrderTotalHours());
        dto.setOrderTotalHours(encryptionService.encrypt(totalHours));

        String totalWorkers = String.valueOf(order.getOrderTotalWokers());
        dto.setOrderTotalWorkers(encryptionService.encrypt(totalWorkers));

        String userId = String.valueOf(order.getCommonUser().getCommonUserId());
        dto.setCommonUserId(encryptionService.encrypt(userId));
        return dto;
    }

    public OrderInfoDto toOrderInfoDto(Order order) throws Exception {
        OrderResponseDto baseDto = toOrderResponseDto(order);
        OrderInfoDto adminDto = new OrderInfoDto();

        List<String> encryptedPackageNames = orderPackageRepository.findByOrderOrderId(order.getOrderId())
                .stream()
                .map(orderPackage -> {
                    try {
                        return encryptionService.encrypt(orderPackage.getPackage().getPackageName());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        if (!encryptedPackageNames.isEmpty()) {
            adminDto.setPackageNames(encryptedPackageNames);
        } else {
            adminDto.setPackageNames(null);
        }

        List<String> encriptedComboNames = orderComboRepository.findByOrderOrderId(order.getOrderId())
                .stream()
                .map(orderCombo -> {
                    try {
                        return encryptionService.encrypt(orderCombo.getCombo().getComboName());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        if (!encriptedComboNames.isEmpty()) {
            adminDto.setComboNames(encriptedComboNames);
        } else {
            adminDto.setComboNames(null);
        }


        List<String> encryptedWorkerNames = workerOrderRepository.findByOrderOrderId(order.getOrderId())
                .stream()
                .map(workerOrder -> {
                    try {
                        return encryptionService.encrypt(workerOrder.getWorker().getWorkerName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        if (!encryptedWorkerNames.isEmpty()) {
            adminDto.setWorkerNames(encryptedWorkerNames);
        } else {
            adminDto.setWorkerNames(null);
        }

        BeanUtils.copyProperties(baseDto, adminDto);
        adminDto.setUserName(encryptionService.encrypt(order.getCommonUser().getUserName()));
        adminDto.setUserFirstLastName(encryptionService.encrypt(order.getCommonUser().getUserFirstLastName()));
        adminDto.setUserSecondLastName(encryptionService.encrypt(order.getCommonUser().getUserSecondLastName()));
        adminDto.setUserEmail(encryptionService.encrypt(order.getCommonUser().getUserEmail()));
        adminDto.setUserCellphone(encryptionService.encrypt(order.getCommonUser().getUserCellphone()));
        return adminDto;
    }
}