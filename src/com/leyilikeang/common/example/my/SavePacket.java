package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.util.PcapPacketArrayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likang
 * @date 2018/10/24 11:20
 * <p>
 * demo：将List<PcapPacket>中的数据包保存到.cap文件中
 */
public class SavePacket {

    public static void main(String[] args) {
        final List<PcapPacket> packets = new ArrayList<PcapPacket>();
        final String file = "";
        System.out.printf("Opening file for reading: %s%n", file);
        Pcap pcap = PcapUtils.readOffline(file);
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

        PcapPacketArrayList pcapPacketArrayList = new PcapPacketArrayList(packets);

        PcapUtils.getAllDevs();
        PcapUtils.index = 0;
        PcapUtils.useDev();

        String outFile = "D:/test.cap";
        PcapDumper dumper = PcapUtils.pcap.dumpOpen(outFile);
        for (PcapPacket packet : pcapPacketArrayList) {
            dumper.dump(packet);
        }
        File out = new File(outFile);
        dumper.close();
        PcapUtils.pcap.close();
    }
}
