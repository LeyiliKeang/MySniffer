package com.leyilikeang.common.util;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/7 22:19
 */
public class PcapUtils {

    public static Pcap pcap;
    public static Integer index;
    public static List<PcapIf> alldevs;
    public static StringBuilder errbuf = new StringBuilder();
    public static HashMap<Integer, HashMap<String, String>> ipMacMap = new HashMap<Integer, HashMap<String, String>>();
    public static String gatewayIp;
    public static String gatewayMac;

    public static List<PcapIf> getAllDevs() {
        alldevs = new ArrayList<PcapIf>();  // 用于存放网卡设备的列表

        int r = Pcap.findAllDevs(alldevs, errbuf);  // 搜索本机网卡设备
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            // 没有可用设备时输出错误信息
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return null;
        }

        return alldevs;
    }

    public static void useDev() {
        PcapIf device = alldevs.get(index); // 在网卡设备列表中选择一个设备

        int snaplen = 64 * 1024;    // 截获65536字节以内的数据包
        int flags = Pcap.MODE_PROMISCUOUS;  // 将网卡工作模式设置为混杂模式
        int timeout = 10 * 1000;    // 超时时间设置为10s
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);    // 获取Pcap对象
        if (pcap == null) {
            // 未获取成功输出错误信息
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }
    }

    public static boolean filter(String expression, Pcap pcap) {
        expression = expression.toLowerCase();
        PcapBpfProgram filter = new PcapBpfProgram();
        int optimize = 0;
        int netmask = 0;
        if (expression.equalsIgnoreCase(ConstantUtils.Protocol.LLC.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.LLC.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.HTTP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.HTTP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.SIP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.SIP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.SDP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.SDP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.DNS.getValue())) {
            expression = "port 53";
        }
        int r = pcap.compile(filter, expression, optimize, netmask);
        if (r != Pcap.OK) {
            return false;
        }
        pcap.setFilter(filter);
        return true;
    }

    public static Pcap readOffline(String file) {
        Pcap offlinePcap = Pcap.openOffline(file, errbuf);
        if (offlinePcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
        }
        return offlinePcap;
    }

    public static Pcap saveOffline() {
        PcapIf device = alldevs.get(0);
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return null;
        }
        return pcap;
    }

    public static void arpRequest() {
        String sourceMac = PcapUtils.ipMacMap.get(index).get("MAC").toLowerCase().replace("-", " ");
        String sourceIp = PcapUtils.ipMacMap.get(index).get("IP");
        String prefixIp = sourceIp.substring(0, sourceIp.lastIndexOf(".") + 1);
        sourceIp = ConvertUtils.ipToHex(sourceIp);
        String destinationMac = "ff ff ff ff ff ff";

        String request = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_REQUEST.getValue()
                + sourceMac + sourceIp + "00 00 00 00 00 00";
//                + destinationIp
//                + "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";


//        PcapIf device = alldevs.get(index);
//
//        int snaplen = 64 * 1024;
//        int flags = Pcap.MODE_PROMISCUOUS;
//        int timeout = 10 * 1000;
//        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 64; i++) {
                    String ping = prefixIp + i;
                    String cmd = "ping " + ping;
                    Runtime run = Runtime.getRuntime();
                    try {
                        run.exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 发送ARP请求包
//                    String destinationIp = ConvertUtils.ipToHex(prefixIp + i);
//                    String arpRequest = request;
//                    arpRequest += destinationIp + "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
//                    JPacket packet = new JMemoryPacket(JProtocol.ETHERNET_ID, arpRequest);
//                    packet.recalculateAllChecksums();
//                    packet.scan(Ethernet.ID);
//                    for (int j = 0; j < 4; j++) {
//                        if (pcap.sendPacket(packet) != Pcap.OK) {
//                            System.out.println(pcap.getErr());
//                        }
//                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 64; i < 128; i++) {
                    String ping = prefixIp + i;
                    String cmd = "ping " + ping;
                    Runtime run = Runtime.getRuntime();
                    try {
                        run.exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 128; i < 192; i++) {
                    String ping = prefixIp + i;
                    String cmd = "ping " + ping;
                    Runtime run = Runtime.getRuntime();
                    try {
                        run.exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 192; i < 255; i++) {
                    String ping = prefixIp + i;
                    String cmd = "ping " + ping;
                    Runtime run = Runtime.getRuntime();
                    try {
                        run.exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void arpResponse(Pcap pcap, String sourceIp, String sourceMac, String destinationIp, String destinationMac,
                                   String realMac) {
        String responseToOther = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        String responseToMe;
        if (realMac == null) {
            responseToMe = sourceMac + destinationMac
                    + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                    + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                    + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                    + ConstantUtils.Arp.MAC_LENGTH.getValue()
                    + ConstantUtils.Arp.IP_LENGTH.getValue()
                    + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                    + destinationMac + destinationIp + sourceMac
                    + ConvertUtils.ipToHex(PcapUtils.ipMacMap.get(index).get("IP"));
        } else {
            responseToMe = sourceMac + realMac
                    + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                    + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                    + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                    + ConstantUtils.Arp.MAC_LENGTH.getValue()
                    + ConstantUtils.Arp.IP_LENGTH.getValue()
                    + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                    + realMac + sourceIp + sourceMac
                    + ConvertUtils.ipToHex(PcapUtils.ipMacMap.get(index).get("IP"));
        }

        JPacket responseToOtherPacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToOther);
        JPacket responseToMePacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToMe);

        System.out.println(responseToOtherPacket.toString());
        System.out.println(responseToMePacket.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (pcap.sendPacket(responseToOtherPacket) != Pcap.OK) {
//                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    if (pcap.sendPacket(responseToMePacket) != Pcap.OK) {
//                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void forward(Pcap pcap, String sourceIp, String sourceMac) {
        String gatewayMac = PcapUtils.gatewayMac.replace("-", " ");
        String localMac = PcapUtils.ipMacMap.get(index).get("MAC").toLowerCase().replace("-", " ");
        PcapPacketHandler pcapPacketHandler = new PcapPacketHandler() {

            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {

                JPacket packet = new JMemoryPacket(Ethernet.ID, pcapPacket);
                Ethernet ethernet = packet.getHeader(new Ethernet());
                Ip4 ip4 = new Ip4();
                if (packet.hasHeader(ip4)) {
                    if (FormatUtils.ip(ip4.source()).equals(sourceIp) && Arrays.equals(ethernet.destination(),
                            ConvertUtils.macToByteArray(localMac, " "))) {
                        byte[] sourceMacByte = ConvertUtils.macToByteArray(localMac, " ");
                        byte[] destinationMacByte = ConvertUtils.macToByteArray(gatewayMac, " ");
                        ethernet.source(sourceMacByte);
                        ethernet.destination(destinationMacByte);
                        ethernet.checksum(ethernet.calculateChecksum());
                        packet.scan(Ethernet.ID);
                        if (PcapUtils.pcap.sendPacket(packet) != Pcap.OK) {
                            System.out.println(PcapUtils.pcap.getErr());
                        }
                    }
                    if (FormatUtils.ip(ip4.destination()).equals(sourceIp) && Arrays.equals(ethernet.source(),
                            ConvertUtils.macToByteArray(gatewayMac, " "))) {
                        byte[] sourceMacByte = ConvertUtils.macToByteArray(localMac, " ");
                        byte[] destinationMacByte = ConvertUtils.macToByteArray(sourceMac, " ");
                        ethernet.source(sourceMacByte);
                        ethernet.destination(destinationMacByte);
                        ethernet.checksum(ethernet.calculateChecksum());
                        packet.scan(Ethernet.ID);
                        if (pcap.sendPacket(packet) != Pcap.OK) {
                            System.out.println(PcapUtils.pcap.getErr());
                        }
                    }
                } else {
                    return;
                }
            }
        };
        pcap.loop(-1, pcapPacketHandler, "likang");
    }
}
