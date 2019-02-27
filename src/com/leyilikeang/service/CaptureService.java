package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.PcapDumper;

import java.io.File;

/**
 * @author likang
 * @date 2018/9/7 22:01
 */
public class CaptureService {

    public void capture(MainFrame mainFrame) {
        final MyPacketHandler<PcapDumper> packetHandler = new MyPacketHandler<PcapDumper>(mainFrame);

        final String offFile = "D:/ttttttttt.cap";
        final PcapDumper dumper = PcapUtils.pcap.dumpOpen(offFile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                PcapUtils.pcap.loop(-1, packetHandler, dumper);
                File file = new File(offFile);
                dumper.close();
            }
        }).start();
    }

    public void stop() {
        PcapUtils.pcap.close();
        PcapUtils.useDev();
        PacketUtils.protocolType = null;
    }
}
