package com.shd.cloud.iot.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class Utils {

    public static long calculateDiffTime(long now, LocalDateTime before){
        return now - before.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    public static boolean topicValidator(String topic){
        String t[] = topic.split("/");
        if (t.length != 4) return false;
        if(!t[0].equals("device") && !t[0].equals("healthCheck")) return false;
        if(!t[3].equals("sensor") && !t[3].equals("operator")) return false;
        try {
            UUID uuid = UUID.fromString(t[2]);
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
