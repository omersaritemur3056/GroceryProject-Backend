package com.example.grocery.webApi.responses.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.grocery.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllOrderResponse {

    private int id;

    private LocalDateTime createdDate;

    private LocalDateTime deliveredDate;

    private OrderStatus orderStatus;

    private int paymentId;

    private int customerId;

    private List<Integer> productIds;
}
