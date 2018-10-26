package com.leyilikeang.common.util;

import org.jnetpcap.util.PcapPacketArrayList;

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

    public static PcapPacketArrayList savePackets = new PcapPacketArrayList();

    public static PcapPacketArrayList allPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList ethernetPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList llc2Packets = new PcapPacketArrayList();
    public static PcapPacketArrayList arpPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList icmpPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList ip4Packets = new PcapPacketArrayList();
    public static PcapPacketArrayList ip6Packets = new PcapPacketArrayList();
    public static PcapPacketArrayList tcpPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList httpPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList udpPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList dnsPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList sipPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList sdpPackets = new PcapPacketArrayList();

    public static PcapPacketArrayList allFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList ethernetFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList llc2FilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList arpFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList icmpFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList ip4FilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList ip6FilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList tcpFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList httpFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList udpFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList dnsFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList sipFilterPackets = new PcapPacketArrayList();
    public static PcapPacketArrayList sdpFilterPackets = new PcapPacketArrayList();

    private PacketUtils() {
    }

    public static void capClear() {
        allPackets.clear();
        ethernetPackets.clear();
        llc2Packets.clear();
        arpPackets.clear();
        icmpPackets.clear();
        ip4Packets.clear();
        ip6Packets.clear();
        tcpPackets.clear();
        httpPackets.clear();
        udpPackets.clear();
        dnsPackets.clear();
        sipPackets.clear();
        sdpPackets.clear();
    }

    public static void filterClear() {
        allFilterPackets.clear();
        ethernetFilterPackets.clear();
        llc2FilterPackets.clear();
        arpFilterPackets.clear();
        icmpFilterPackets.clear();
        ip4FilterPackets.clear();
        ip6FilterPackets.clear();
        tcpFilterPackets.clear();
        httpFilterPackets.clear();
        udpFilterPackets.clear();
        dnsFilterPackets.clear();
        sipFilterPackets.clear();
        sdpFilterPackets.clear();
    }
}
