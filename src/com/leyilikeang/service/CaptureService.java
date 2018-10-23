package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.ExpressionPacketHandler;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;

/**
 * @author likang
 * @date 2018/9/7 22:01
 */
public class CaptureService {

    public void capture(MainFrame mainFrame) {
        final ExpressionPacketHandler packetHandler = new ExpressionPacketHandler(mainFrame);

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
