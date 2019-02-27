package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.ConvertUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.util.Arrays;

/**
 * Created by Goldmsg on 2018/10/22.
 */
public class Forward {

    public static void main(String[] args) {
        final String destinationMac = "c4 36 55 92 c0 05";
        final String destinationIp = "c0 a8 00 01";
        final String sourceMac = "60 d8 19 2c f2 62";
        final String sourceIp = "192.168.0.10";

        PcapUtils.getAllDevs();
        PcapUtils.index = 2;
        PcapUtils.useDev();
        PcapPacketHandler pcapPacketHandler = new PcapPacketHandler() {

            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {
//                System.out.println(pcapPacket.toString());

                final JPacket packet = new JMemoryPacket(Ethernet.ID, pcapPacket);
                /**
                 * 转发数据包
                 *
                 */
                Ethernet ethernet = packet.getHeader(new Ethernet());
                Ip4 ip4 = new Ip4();
                if (packet.hasHeader(ip4)) {
                    if (FormatUtils.ip(ip4.source()).equals(sourceIp) && Arrays.equals(ethernet.destination(),
                            ConvertUtils.macToByteArray("40 e2 30 df f9 ff", " "))) {
//                        System.out.println(pcapPacket.toString());
                        byte[] sourceMacByte = ConvertUtils.macToByteArray("40 e2 30 df f9 ff", " ");
                        byte[] destinationMacByte = ConvertUtils.macToByteArray(destinationMac, " ");
                        ethernet.source(sourceMacByte);
                        ethernet.destination(destinationMacByte);
                        ethernet.checksum(ethernet.calculateChecksum());
//                        System.out.println(packet.toString());
                        packet.scan(Ethernet.ID);
                        if (PcapUtils.pcap.sendPacket(packet) != Pcap.OK) {
                            System.out.println(PcapUtils.pcap.getErr());
                        }
                    }
                    if (FormatUtils.ip(ip4.destination()).equals(sourceIp) && Arrays.equals(ethernet.source(),
                            ConvertUtils.macToByteArray(destinationMac, " "))) {
//                        System.out.println(pcapPacket.toString());
                        byte[] sourceMacByte = ConvertUtils.macToByteArray("40 e2 30 df f9 ff", " ");
                        byte[] destinationMacByte = ConvertUtils.macToByteArray(sourceMac, " ");
                        ethernet.source(sourceMacByte);
                        ethernet.destination(destinationMacByte);
                        ethernet.checksum(ethernet.calculateChecksum());
//                        System.out.println(packet.toString());
                        packet.scan(Ethernet.ID);
                        if (PcapUtils.pcap.sendPacket(packet) != Pcap.OK) {
                            System.out.println(PcapUtils.pcap.getErr());
                        }
                    }
                } else {
                    return;
                }
            }
        };
        PcapUtils.pcap.loop(-1, pcapPacketHandler, "likang");
        PcapUtils.pcap.close();
    }
}
