package com.priye.razorpay.payment.mapper;

import com.priye.razorpay.payment.dto.response.OrderResponse;
import com.priye.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
