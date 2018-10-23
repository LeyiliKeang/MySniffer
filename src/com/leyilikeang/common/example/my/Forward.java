package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.ConvertUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

/**
 * Created by Goldmsg on 2018/10/22.
 */
public class Forward {

    public static void main(String[] args) {
        final String destinationMac = "ec 26 ca ff 9d 36";
        final String destinationIp = "c0 a8 01 01";
        final String sourceMac = "40 e2 30 df f9 ff";
        String sourceIp = "c0 a8 01 66";

        PcapUtils.getAllDevs();
        PcapUtils.index = 2;
        PcapUtils.useDev();
        PcapPacketHandler pcapPacketHandler = new PcapPacketHandler() {

            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {
                System.out.println(pcapPacket.toString());

                final JPacket packet = new PcapPacket(pcapPacket);
                /**
                 * 转发数据包
                 */
                Ip4 ip4 = new Ip4();
                if (packet.hasHeader(ip4)) {
                    if (ip4.destination().equals(destinationIp)) {
                        Ethernet ethernet = packet.getHeader(new Ethernet());
                        byte[] sourceMacByte = ConvertUtils.macToByteArray(sourceMac, " ");
                        byte[] destinationMacByte = ConvertUtils.macToByteArray(destinationMac, " ");
                        ethernet.source(sourceMacByte);
                        ethernet.destination(destinationMacByte);
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (PcapUtils.pcap.sendPacket(packet) != Pcap.OK) {
                                System.out.println(PcapUtils.pcap.getErr());
                            }
                        }
                    }
                }).start();
                System.out.println(packet.toString());
            }
        };
        PcapUtils.pcap.loop(1, pcapPacketHandler, "likang");
        PcapUtils.pcap.close();
    }
}
