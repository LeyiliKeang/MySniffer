package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.PcapPacket;

import java.io.File;

/**
 * @author likang
 * @date 2018/10/26 14:10
 */
public class FileService {

    public void save(String path) {
        Pcap pcap = PcapUtils.saveOffline();
        PcapDumper dumper = pcap.dumpOpen(path);
        for (PcapPacket packet : PacketUtils.allPackets) {
            dumper.dump(packet);
        }
        File file = new File(path);
        dumper.close();
        pcap.close();
    }

    public void open(MainFrame mainFrame, String path) {
        String expression = mainFrame.getSourceIpTextField().getText();
        mainFrame.getDefaultTableModel().setRowCount(0);
        PacketUtils.capClear();
        final Pcap pcap = PcapUtils.readOffline(path);
        PcapBpfProgram filter = new PcapBpfProgram();
        int optimize = 0;
        int netmask = 0;
        int r = pcap.compile(filter, expression, optimize, netmask);
        if (r != Pcap.OK) {
            return;
        }
        pcap.setFilter(filter);
        final MyPacketHandler packetHandler = new MyPacketHandler(mainFrame);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pcap.loop(-1, packetHandler, "likang");
            }
        }).start();
    }
}
