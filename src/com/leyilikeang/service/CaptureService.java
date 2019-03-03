package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.FileUtils;
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
    public static boolean isStart = false;

    public void capture(MainFrame mainFrame) {
        final MyPacketHandler<PcapDumper> packetHandler = new MyPacketHandler<PcapDumper>(mainFrame);

        final PcapDumper dumper = PcapUtils.pcap.dumpOpen(FileUtils.tempFile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                isStart = true;
                PcapUtils.pcap.loop(-1, packetHandler, dumper);
                File file = new File(FileUtils.tempFile);
                dumper.close();
            }
        }).start();
    }

    public void stop() {
        isStart = false;
        PcapUtils.pcap.close();
        PcapUtils.useDev();
        PacketUtils.protocolType = null;
    }
}
