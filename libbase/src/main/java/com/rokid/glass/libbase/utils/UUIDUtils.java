package com.rokid.glass.libbase.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr.replaceAll("-","");
    }
}
