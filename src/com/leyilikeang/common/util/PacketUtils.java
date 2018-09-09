package com.leyilikeang.common.util;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author likang
 * @date 2018/9/8 10:53
 */
public class PacketUtils {

    /**
     * 每次抓包的序号标记
     */
    public static Integer count = 0;

    /**
     * 存储所有包
     */
    public static Map<Integer, JPacket> allMap = new HashMap<Integer, JPacket>();

    /**
     * 存储不同协议的包，用于对协议分类查看
     */
    public static Map<String, Map<Integer, PcapPacket>> diffMap = new HashMap<String, Map<Integer, PcapPacket>>();

    public static Map<Integer, JPacket> arpMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> ipMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> icmpMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> tcpMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> httpMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> udpMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> sipMap = new HashMap<Integer, JPacket>();

    public static Map<Integer, JPacket> sdpMap = new HashMap<Integer, JPacket>();

    public static void clear() {
        count = 0;
        allMap.clear();
    }
}
