package com.hotel.Vnpay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class VnPayService {
    private final VnPayConfig vnPayConfig;


    public String createUrl(VnPayModel vnPayModel) throws Exception {
        //tạo ra  map chứa các thuộc tính
        Map<String, String> vnp_Params = new HashMap<>();
        // đâẩn các thuộc tính  vào map
        vnp_Params.put("vnp_Version", vnPayConfig.getVnpVersion());
        vnp_Params.put("vnp_Command", vnPayConfig.getVnpCommand());
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getVnp_TmnCode());
        String amount = vnPayModel.getPrice().multiply(new BigDecimal("100"))
                .toBigInteger()
                .toString();
        vnp_Params.put("vnp_Amount", amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_Locale", Optional.ofNullable(vnPayModel.getLanguage()).orElse("vn"));
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnPayModel.getIpAddress());
        vnp_Params.put("vnp_TxnRef", vnPayModel.getVnTxnRef());
        vnp_Params.put("vnp_OrderInfo", vnPayModel.getOrderInfo());
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", createDate);
        cld.add(Calendar.MINUTE, 15);
        String expireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", expireDate);
        //Sắp xếp lại các trường theo field name
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Phải mã hóa giá trị (fieldValue) trước khi đưa vào hashData
                // Dùng UTF-8 để hỗ trợ tiếng Việt
                String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                hashData.append(fieldName).append('=').append(encodedFieldValue);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                        .append('=')
                        .append(encodedFieldValue);
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }
        // hash query
        String vnp_SecureHash = VnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        String queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
        System.out.println("DEBUG hashData: " + hashData.toString());
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }


    public boolean verifySignature(Map<String, String> params) {
        if (params == null || params.isEmpty()) return false;
        String secureHash = params.get("vnp_SecureHash");
        if (secureHash == null || secureHash.isBlank()) return false;
        Map<String, String> filteredParams = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (!key.equalsIgnoreCase("vnp_SecureHash") && !key.equalsIgnoreCase("vnp_SecureHashType")) {
                filteredParams.put(key, entry.getValue());
            }
        }
        List<String> fieldNames = new ArrayList<>(filteredParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            String value = filteredParams.get(key);
            if (value != null && !value.isEmpty()) {
                try {
                    String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII.toString());
                    hashData.append(key).append("=").append(encodedValue);
                    if (i < fieldNames.size() - 1) {
                        hashData.append("&");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        String computedHash = VnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        return secureHash.equalsIgnoreCase(computedHash);
    }
}
