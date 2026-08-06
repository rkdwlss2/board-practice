package com.example.boardpractice.common.utill;

import jakarta.servlet.http.HttpServletRequest;

public class ClientUtils {
    public static String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 사용 시 여러 IP가 넘어올 경우 첫 번째 IP가 실제 사용자 IP입니다.
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
