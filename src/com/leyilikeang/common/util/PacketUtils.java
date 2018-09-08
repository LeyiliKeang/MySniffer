package com.leyilikeang.common.util;

import org.jnetpcap.packet.PcapPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author likang
 * @date 2018/9/8 10:53
 */
public class PacketUtils {

    public static Integer count = 0;

    public static Map<Integer, PcapPacket> map = new HashMap<Integer, PcapPacket>();

    public static void clear() {
        count = 0;
        map.clear();
    }
}
