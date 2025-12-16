package com.hotel.Vnpay;


import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Component
public class VnPayConfig {
    @Value("${vnpay.url}")
    private String vnp_PayUrl;
    @Value("${vnpay.returnurl}")
    private String vnp_ReturnUrl;
    @Value("${vnpay.terminalcode}")
    private String vnp_TmnCode;
    @Value("${vnpay.secretkey}")
    private String secretKey;
    @Value("${vnpay.apiurl}")
    private String vnp_ApiUrl;
    @Value("${vnpay.version:2.1.0}")
    private String vnpVersion;
    @Value("${vnpay.command:pay}")
    private String vnpCommand;
    // ✅ Mã hóa HMAC SHA512
    public static String hmacSHA512(final String key, final String data) {
        try {
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(result.length * 2);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    // ✅ Lấy IP
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) ip = request.getRemoteAddr();
        return ip;
    }

    // ✅ Sinh mã ngẫu nhiên
    public static String getRandomNumber(int len) {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // ✅ Tạo URL query (dạng key=value&key=value)
    public static String buildQueryUrl(Map<String, String> params, boolean encode) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> (encode ? URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) : e.getKey()) +
                        "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }


    // ✅ hashAllFields - sử dụng secretKey của instance
    public String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                sb.append(fieldName).append('=').append(fieldValue);
                if (itr.hasNext()) sb.append('&');
            }
        }
        return hmacSHA512(this.secretKey, sb.toString());
    }
}