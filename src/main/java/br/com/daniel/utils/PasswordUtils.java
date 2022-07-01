package br.com.daniel.utils;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtils {
    private static final Log log = LogFactory.getLog(PasswordUtils.class);
    private static final int HEXADECIMAL = 16;

    private PasswordUtils() {
    }

    public static boolean matches(final String rawPassword, final String hashedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(hashedPassword))
            return false;
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawPassword.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md5.digest()).toString(HEXADECIMAL).equals(hashedPassword);
        } catch (Exception ex) {
            log.error(String.format("error trying to verify passwords: %s", ex.getMessage()));
            return false;
        }
    }

    public static String hash(final String rawPassword) {
        if (!StringUtils.hasText(rawPassword))
            return "";
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawPassword.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md5.digest()).toString(HEXADECIMAL);
        } catch (Exception ex) {
            log.error(String.format("error trying to hash password: %s", ex.getMessage()));
            return "";
        }
    }
}
