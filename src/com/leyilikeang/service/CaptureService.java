package com.leyilikeang.service;

import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import javax.swing.table.DefaultTableModel;
import java.util.Date;

/**
 * @author likang
 * @date 2018/9/7 22:01
 */
public class CaptureService {

    public void capture(final DefaultTableModel defaultTableModel) {
        final PcapPacketHandler packetHandler = new PcapPacketHandler() {
            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {
                System.out.printf("Received packet at %s caplen = %-4d len = %-4d %s\n",
                        new Date(pcapPacket.getCaptureHeader().timestampInMillis()),
                        pcapPacket.getCaptureHeader().wirelen(),
                        pcapPacket.getCaptureHeader().wirelen(),
                        o);
                PacketUtils.allMap.put(PacketUtils.count, pcapPacket);
                defaultTableModel.addRow(new Object[]{++PacketUtils.count, "192.168", "80", "172.16", "8080", "666", pcapPacket.getPacketWirelen()});
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                PcapUtils.pcap.loop(-1, packetHandler, "jNetPcap rocks!");
            }
        }).start();
    }

    public void stop() {
        PcapUtils.pcap.close();
        PcapUtils.useDev();
    }
}
