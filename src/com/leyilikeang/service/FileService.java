package com.leyilikeang.service;

import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
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
}
