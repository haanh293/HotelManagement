package com.hotel.Vnpay;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VnPayDTO {
   private Integer code;
   private Long orderId;
}
