package com.brokerapp.util;

import com.brokerapp.constant.Constant;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public final class Util {

    private Util() {}

    public static boolean isAdmin(UsernamePasswordAuthenticationToken principal) {
        return !((List<?>) principal.getAuthorities())
                .get(0)
                .toString()
                .equals(Constant.ROLE_ADMIN);
    }
}
