package com.hotel.Vnpay;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VnPayModel {
    private String vnTxnRef;
    private BigDecimal price;
    private String orderInfo;
    private String language; // Tùy chọn
    private String ipAddress;
}
