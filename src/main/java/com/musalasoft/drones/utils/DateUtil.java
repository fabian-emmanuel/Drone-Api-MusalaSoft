package com.musalasoft.drones.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DateUtil {
    static String formatLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");
        return localDateTime.format(formatter);
    }
}
