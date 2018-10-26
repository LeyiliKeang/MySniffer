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

    private PacketUtils() {
    }

    public static void clear() {
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
}
