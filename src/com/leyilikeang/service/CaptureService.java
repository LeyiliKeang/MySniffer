package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.PcapUtils;

import javax.swing.table.DefaultTableModel;

/**
 * @author likang
 * @date 2018/9/7 22:01
 */
public class CaptureService {

    public void capture(final DefaultTableModel defaultTableModel) {
        defaultTableModel.setRowCount(0);

        final MyPacketHandler packetHandler = new MyPacketHandler(defaultTableModel);

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
