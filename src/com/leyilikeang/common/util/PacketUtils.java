package com.leyilikeang.common.util;

import org.jnetpcap.util.PcapPacketArrayList;

/**
 * @author likang
 * @date 2018/9/8 10:53
 */
public class PacketUtils {

    public static String protocolType;

    public static Integer per = 0;

    public static PcapPacketArrayList savePackets = new PcapPacketArrayList();

    public static PcapPacketArrayList allPackets = new PcapPacketArrayList();
    public static Integer ethernetAmount = 0;
    public static Integer llc2Amount = 0;
    public static Integer arpAmount = 0;
    public static Integer icmpAmount = 0;
    public static Integer ip4Amount = 0;
    public static Integer ip6Amount = 0;
    public static Integer tcpAmount = 0;
    public static Integer httpAmount = 0;
    public static Integer udpAmount = 0;
    public static Integer dnsAmount = 0;
    public static Integer sipAmount = 0;
    public static Integer sdpAmount = 0;

    private PacketUtils() {
    }

    public static void capClear() {
        allPackets.clear();
        ethernetAmount = 0;
        llc2Amount = 0;
        arpAmount = 0;
        icmpAmount = 0;
        ip4Amount = 0;
        ip6Amount = 0;
        tcpAmount = 0;
        httpAmount = 0;
        udpAmount = 0;
        dnsAmount = 0;
        sipAmount = 0;
        sdpAmount = 0;
    }
}
