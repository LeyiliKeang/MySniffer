package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.util.PcapPacketArrayList;

import java.util.ArrayList;

/**
 * @author likang
 * @date 2018/10/24 11:31
 *
 *
 */
public class OfflineFilter {

    public static void main(String[] args) {
        final ArrayList<PcapPacket> packets = new ArrayList<PcapPacket>();
        final String file = "D:/tmp-capture-file.cap";
        System.out.printf("Opening file for reading: %s%n", file);
        final Pcap pcap = PcapUtils.readOffline(file);
        PcapPacketHandler packetHandler = new PcapPacketHandler() {
            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {
                packets.add(pcapPacket);
            }
        };
        try {
            pcap.loop(-1, packetHandler, "jNetPcap rocks!");
        } finally {
            pcap.close();
        }


        PcapUtils.getAllDevs();
        PcapUtils.index = 0;
        PcapUtils.useDev();

        PcapUtils.filter("port 443");

        PcapPacketArrayList pcapPacketArrayList = new PcapPacketArrayList(packets);


        PcapUtils.pcap.close();
        try {
            PcapUtils.pcap.loop(pcapPacketArrayList.size(), pcapPacketArrayList, "");
        } finally {
            PcapUtils.pcap.close();
        }
    }
}
