package com.leyilikeang.common.util;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.util.PcapPacketArrayList;

import java.util.ArrayList;

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

    public static PcapPacketArrayList allFilterPackets = new PcapPacketArrayList();
    public static Integer ethernetFilterAmount = 0;
    public static Integer llc2FilterAmount = 0;
    public static Integer arpFilterAmount = 0;
    public static Integer icmpFilterAmount = 0;
    public static Integer ip4FilterAmount = 0;
    public static Integer ip6FilterAmount = 0;
    public static Integer tcpFilterAmount = 0;
    public static Integer httpFilterAmount = 0;
    public static Integer udpFilterAmount = 0;
    public static Integer dnsFilterAmount = 0;
    public static Integer sipFilterAmount = 0;
    public static Integer sdpFilterAmount = 0;

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

    public static void filterClear() {
        allFilterPackets.clear();
        ethernetFilterAmount = 0;
        llc2FilterAmount = 0;
        arpFilterAmount = 0;
        icmpFilterAmount = 0;
        ip4FilterAmount = 0;
        ip6FilterAmount = 0;
        tcpFilterAmount = 0;
        httpFilterAmount = 0;
        udpFilterAmount = 0;
        dnsFilterAmount = 0;
        sipFilterAmount = 0;
        sdpFilterAmount = 0;
    }
}
