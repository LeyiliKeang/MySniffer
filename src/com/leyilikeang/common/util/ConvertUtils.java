package com.leyilikeang.common.util;

/**
 * Created by Goldmsg on 2018/10/22.
 */
public class ConvertUtils {

    private ConvertUtils() {
    }

    public static byte[] macToByteArray(String mac, String splitter) {
        String[] macChip = mac.split(splitter);
        byte[] macByte = new byte[6];
        for (int i = 0; i < macChip.length; i++) {
            int macInt = Integer.valueOf(String.valueOf(macChip[i]), 16);
            if (macInt > 127) {
                macByte[i] = (byte) (macInt - 256);
            } else {
                macByte[i] = (byte) macInt;
            }
        }
        return macByte;
    }
}
