package br.com.daniel.utils;

import org.springframework.util.StringUtils;

public class Base64Utils {
    private Base64Utils() {
    }

    public static String decode(final String source) {
        if (StringUtils.hasText(source)) return new String(org.springframework.util.Base64Utils.decodeFromString(source));
        return "";
    }
}
