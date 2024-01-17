package com.musalasoft.drones.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public interface GeneratorUtil {

    static String uniqueReference(String prefix, int length) {
        String uuid = UUID.randomUUID().toString();
        String md5Digest = DigestUtils.md5DigestAsHex(uuid.getBytes()).toUpperCase();

        int actualLength = Math.min(length, md5Digest.length());

        return String.format("%s_%s", prefix, md5Digest.substring(0, actualLength));
    }
}
