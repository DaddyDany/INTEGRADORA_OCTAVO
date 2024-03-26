package utez.edu.mx.orderapp.services.orders;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import utez.edu.mx.orderapp.controllers.orders.OrderInfoAdminDto;
import utez.edu.mx.orderapp.controllers.orders.OrderResponseDto;
import utez.edu.mx.orderapp.models.orders.Order;

@Service
public class OrderMapper {

    public static OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderState(order.getOrderState());
        dto.setOrderPlace(order.getOrderPlace());
        dto.setOrderTime(order.getOrderTime());
        dto.setOrderTotalPayment(order.getOrderTotalPayment());
        dto.setOrderPaymentState(order.getOrderPaymentState());
        dto.setOrderType(order.getOrderType());
        dto.setOrderTotalHours(order.getOrderTotalHours());
        dto.setCommonUserId(order.getCommonUser().getCommonUserId());
        return dto;
    }

    public static OrderInfoAdminDto toOrderInfoAdminDto(Order order) {
        OrderResponseDto baseDto = toOrderResponseDto(order);
        OrderInfoAdminDto adminDto = new OrderInfoAdminDto();
        BeanUtils.copyProperties(baseDto, adminDto);
        adminDto.setUserName(order.getCommonUser().getUserName());
        adminDto.setUserFirstLastName(order.getCommonUser().getUserFirstLastName());
        adminDto.setUserSecondLastName(order.getCommonUser().getUserSecondLastName());
        adminDto.setUserEmail(order.getCommonUser().getUserEmail());
        adminDto.setUserCellphone(order.getCommonUser().getUserCellphone());
        return adminDto;
    }
}