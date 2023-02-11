package com.shd.cloud.iot.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

    public static long calculateDiffTime(long now, LocalDateTime before){
        return now - before.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
