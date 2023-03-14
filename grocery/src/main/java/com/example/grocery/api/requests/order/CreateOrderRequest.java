package com.example.grocery.api.requests.order;

import com.example.grocery.model.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss:")
    private LocalDateTime deliveredDate;

    @NotNull
    private OrderStatus orderStatus;

    @Min(value = 1)
    private Long paymentId;

    @Min(value = 1)
    private Long customerId;

    @NotNull
    private Long[] productIds;
}
