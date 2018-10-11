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
     * 源IP地址
     */
    public static String sourceIpAddress;

    /**
     * 目标IP地址
     */
    public static String destinationIpAddress;

    /**
     * 源端口号
     */
    public static Integer sourcePort;

    /**
     * 目标端口号
     */
    public static Integer destinationPort;

    /**
     * 源MAC地址
     */
    public static String sourceMacAddress;

    /**
     * 目标MAC地址
     */
    public static String destinationMacAddress;

    /**
     * 每次抓包的序号标记
     */
    public static Integer allCount = 0;

    /**
     * 存储所有包
     */
    public static Map<Integer, JPacket> allMap = new HashMap<Integer, JPacket>();

    /**
     * 存储不同协议的包，用于对协议分类查看
     */
    public static Map<String, Map<Integer, PcapPacket>> diffMap = new HashMap<String, Map<Integer, PcapPacket>>();

    /**
     * arp序号
     */
    public static Integer arpCount = 0;

    /**
     * 用于存储arp包
     */
    public static Map<Integer, JPacket> arpMap = new HashMap<Integer, JPacket>();

    public static Integer ipCount = 0;
    public static Map<Integer, JPacket> ipMap = new HashMap<Integer, JPacket>();

    public static Integer icmpCount = 0;
    public static Map<Integer, JPacket> icmpMap = new HashMap<Integer, JPacket>();

    public static Integer tcpCount = 0;
    public static Map<Integer, JPacket> tcpMap = new HashMap<Integer, JPacket>();

    public static Integer httpCount = 0;
    public static Map<Integer, JPacket> httpMap = new HashMap<Integer, JPacket>();

    public static Integer udpCount = 0;
    public static Map<Integer, JPacket> udpMap = new HashMap<Integer, JPacket>();

    public static Integer sipCount = 0;
    public static Map<Integer, JPacket> sipMap = new HashMap<Integer, JPacket>();

    public static Integer sdpCount = 0;
    public static Map<Integer, JPacket> sdpMap = new HashMap<Integer, JPacket>();

    private PacketUtils() {

    }

    public static void clear() {
        allCount = 0;
        allMap.clear();

        arpCount = 0;
        arpMap.clear();

        ipCount = 0;
        ipMap.clear();

        icmpCount = 0;
        icmpMap.clear();

        tcpCount = 0;
        tcpMap.clear();

        httpCount = 0;
        httpMap.clear();

        udpCount = 0;
        udpMap.clear();

        sipCount = 0;
        sipMap.clear();

        sdpCount = 0;
        sdpMap.clear();
    }
}
