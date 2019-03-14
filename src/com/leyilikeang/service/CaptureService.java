package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.FileUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.JumpToFrame;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.PcapDumper;

import java.io.File;

/**
 * @author likang
 * @date 2018/9/7 22:01
 */
public class CaptureService {
    public static boolean isStart = false;

    public static boolean isOpen = false;

    public void capture(MainFrame mainFrame) {
        MyPacketHandler<PcapDumper> packetHandler = new MyPacketHandler<PcapDumper>(mainFrame);

        PcapDumper dumper = PcapUtils.pcap.dumpOpen(FileUtils.tempFile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                isStart = true; // 设置捕获是否开始标识
                if (JumpToFrame.isOpen) {
                    mainFrame.getJumpToDialog().setVisible(false);
                }
                PcapUtils.pcap.loop(-1, packetHandler, dumper); // 执行捕获，-1代表无限捕获
                File file = new File(FileUtils.tempFile);
                dumper.close();
                if (JumpToFrame.isOpen) {
                    mainFrame.getJumpToDialog().setVisible(true);
                }
            }
        }).start();
    }

    public void stop() {
        isStart = false;
        PcapUtils.pcap.close(); // 关闭数据包捕获捕获
        PcapUtils.useDev();
        PacketUtils.protocolType = null;
    }
}
